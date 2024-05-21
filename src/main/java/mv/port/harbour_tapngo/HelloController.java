package mv.port.harbour_tapngo;

import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.EscPosConst;
import com.github.anastaciocintra.escpos.Style;
import com.github.anastaciocintra.escpos.image.*;
import com.github.anastaciocintra.output.PrinterOutputStream;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import ecrlib_globalpay.Data;
import ecrlib_globalpay.ecrlib;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.Notifications;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.imageio.ImageIO;
import javax.print.PrintService;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;


import static ecrlib_globalpay.ecrlib.BeepIt;


public class HelloController implements Initializable {

    private static final DecimalFormat decimalFormatter = new DecimalFormat("0.00");
    private static final Logger logger = LogManager.getLogger(HelloApplication.class);

    @FXML
    public TextArea logText;
    public ProgressIndicator progressBar;
    public Label progressLabel;
    public ButtonBase payButton;
    public ButtonBase testButton;
    public ChoiceBox<String> printerChoiceBox;
    public ChoiceBox<String> gateChoiceBox;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.populatePrinters();

        String[] gates = {"Gate One", "Gate Two"};
        this.gateChoiceBox.getItems().addAll(gates);

        // clear labels
        this.progressLabel.setText("Click to Initiate");
        this.logText.setEditable(false);
//        this.logText.setMouseTransparent(true);
        this.logText.setFocusTraversable(false);

        // hide test button
        this.testButton.setVisible(false);
    }


    @FXML
    protected void onPayButtonClick() {

        // check whether printer and gate is selected
        String selectedPrinter = printerChoiceBox.getValue();
        String selectedGate = gateChoiceBox.getValue();

        if( selectedPrinter == null || selectedGate == null || selectedPrinter.isEmpty() || selectedGate.isEmpty()){
            showNotification("Warning", "Make sure printer and gate is selected!", NotificationType.Warning, 3000 );
            return;
        }


        Service<Data> scheduledService = new Service<Data>() {
            @Override
            protected Task<Data> createTask() {
                Task<Data> task = new Task<Data>() {

                    @Override
                    public Data call() {

                        logger.info("Initiating sale.");
                        clearLogText();
                        setLogText("Initiating sale.");
                        BeepIt(1000,50);
                        BeepIt(2000,250);
                        BeepIt(2000,250);

                        String amount = "000000000005";

                        ecrlib ecr = new ecrlib();
                        Data data = null;

                        data = ecr.DoSale(200,amount);

                        return data;

//                        return simulate();
                    }

//                    Simulate database delay
//                    private Data simulate() {
//                        try {
//                            Thread.sleep(5000);
//                            return new Data(
//                                    "InvoiceNo",
//                                    "BatchNumver",
//                                    "TerminalID",
//                                    "MerchanID",
//                                    "HostName",
//                                    "ApproveCode",
//                                    "00", // 00 is success,
//                                    "ReferanceNumber",
//                                    "CardNumberBin",
//                                    "CardNumberLast",
//                                     "CardType",
//                                     "Amount",
//                                     "CardName",
//                                     "ErrorCode",
//                                    100
//                            );
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        return null;
//                    }


                };

                task.setOnSucceeded((workerStateEvent) ->
                {
                    progressLabel.setText("Completed...");
                    progressBar.setVisible(false);
                    payButton.setDisable(false);

                    Data result = task.getValue();

                    if (result == null)
                    {
                        logger.error("Must be a communication failure!");
                        setLogText("Must be a communication failure!");
                        return;
                    }else if (HandleResponseCode(result) < 0){
                        BeepIt(1700,100);
                        BeepIt(1500,300);
                        // will show an error msg from Handle Response Method
                        return;
                    }
                    // TODO assert success and proceed

                    // TODO Log all details

                    printAll(result); // success
                    setLogText("Payment Success!");

                    String gate = (String)gateChoiceBox.getValue();


                    // send to harbour
                    Sticker newSticker = new Sticker(2, gate, 1, "POS", 2);
                    newSticker.setTerminal_id(result.TerminalID);
                    newSticker.setBatch_no(result.BatchNumver);
                    newSticker.setCard_no(result.CardNumberLast);
                    newSticker.setCard_type(result.CardType);
                    newSticker.setReference_no(result.ReferanceNumber);
                    newSticker.setTrace_no(result.ReferanceNumber);

                    setLogText("Creating Sticker!");

                    // http post
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://harbour-master.mpltest/") // TEST
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    HarbourService service = retrofit.create(HarbourService.class);

//        Call<Sticker> callSync = service.createSticker(newSticker);
                    Call<StickerResponse> callAsync = service.createSticker(newSticker);

//        try {
//            Sticker sticker = response.get();
//            System.out.println(sticker.toString());
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        } catch (ExecutionException e) {
//            throw new RuntimeException(e);
//        }

                    callAsync.enqueue(new Callback<StickerResponse>() {
                        @Override
                        public void onResponse(Call<StickerResponse> call, Response<StickerResponse> response) {
                            System.out.println(response.code());
                            System.out.println(response.message());
                            System.out.println(response);

                            if(response.isSuccessful() && response.code() == 200){
                                Sticker sticker = response.body().sticker;
                                logger.info("Successfully created sticker, {}", sticker.getSticker_no() );
                                setLogText("Sticker created successfully! " + sticker.getSticker_no() );

                                // print sticker
                                try {
                                    setLogText("Printing Sticker!");
                                    printSticker(sticker);
                                } catch (IOException e) {
                                    logger.error("Error printing sticker", e);
                                }
                            }else{
                                logger.error("Error creating sticker in harbour! {}", response.message());
                                setLogText("Error creating sticker in harbour!");
                                try {
                                    System.out.println(response.errorBody().string());
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
//                                    Gson gson = new Gson();
//                                    Type type = new TypeToken<ErrorResponse>() {}.getType();
//                                    ErrorResponse errorResponse = gson.fromJson(response.errorBody().charStream(),type);
//                                    logger.error(response.errorBody().toString());

                            }
                        }

                        @Override
                        public void onFailure(Call<StickerResponse> call, Throwable throwable) {
                            logger.fatal("Unexpected error creating sticker!", throwable);
                            setLogText("Unexpected error creating sticker!");
//                                throwable.printStackTrace();
                            // TODO update UI
                        }

                    });



                });
                task.setOnRunning((workerStateEvent) ->
                {
                    progressLabel.setText("Waiting...");
                    progressBar.setVisible(true);
                    payButton.setDisable(true);
                });
                progressBar.progressProperty().bind(task.progressProperty());
                return task;

            }
        };

//        scheduledService.setPeriod(Duration.millis(5000))
        scheduledService.start();

    }


    public void setLogText(String line){
        String currentLog = this.logText.getText();
        this.logText.setText(currentLog + line + "\n");

    }

    public void clearLogText(){
        this.logText.clear();
    }

    public int HandleResponseCode(Data data)
    {
        String resCode = new String(data.ResponeseCode);

        if (resCode.equals("00"))
        {
            logger.info("Transaction is successful!!");
            setLogText("Transaction is successful!!");
            return 0;
        }else if (resCode.equals("1A")) {
            logger.warn("Transaction was cancelled or failed in card operation");
            setLogText("Transaction was cancelled or failed in card operation");
        }
        else if (resCode.equals("1B")) {
            logger.warn("Transaction was cancelled or failed in card transaction");
            setLogText("Transaction was cancelled or failed in card transaction");
        }
        else if (resCode.equals("1C")) {
            logger.warn("Transaction was cancelled before the initiation");
            setLogText("Transaction was cancelled before the initiation");
        }
        else {
            logger.error("HOST RESPONSE CODE {}", resCode);
            setLogText("HOST RESPONSE CODE" + resCode);
        }

        return -1;

    }


    public void printAll(Data data)
    {
        logger.info("=============================================");
        logger.info("InvoiceNumber is " + data.InvoiceNumber);
        logger.info("BatchNumver is " + data.BatchNumver);
        logger.info("TerminalID is " + data.TerminalID);
        logger.info("MerchanID is " + data.MerchanID);
        logger.info("HostName is " + data.HostName);
        logger.info("ApproveCode is " + data.ApproveCode);
        logger.info("ResponeseCode is " + data.ResponeseCode);
        logger.info("ReferanceNumber is " + data.ReferanceNumber);
        logger.info("CardNumberBin is " + data.CardNumberBin);
        logger.info("CardNumberLast is " + data.CardNumberLast);
        logger.info("CardType name is " + data.CardType);
        logger.info("szAmount is " + data.Amount);
        logger.info("CardName is " + data.CardName);
        logger.info("ErrorCode is " + data.ErrorCode);
        logger.info("TimeOut is " + data.inTimeOut);
        logger.info("=============================================");
    }


    public enum NotificationType {
        Info,
        Warning,
        Error,
    }

    public static void showNotification(String title, String text, NotificationType notificationType,  int milliSecs) {
        Notifications notification = Notifications.create();
        notification.hideAfter(new Duration(milliSecs));
        notification.title(title);
        notification.text(text);
        notification.position(Pos.TOP_RIGHT);

        Platform.runLater(() -> {
            switch (notificationType){
                case Info -> notification.showInformation();
                case Warning -> notification.showWarning();
                case Error -> notification.showError();
            }
        });
    }

    public void ontestButtonClick(ActionEvent actionEvent) throws IOException {

        showNotification("Test", "This is a toast",NotificationType.Info, 3000);
        showNotification("Test 2", "This is the second toast", NotificationType.Error, 3000);

//
//        Sticker newSticker = new Sticker(2, "Gate One", 1, "POS", 2);
////        newSticker.sticker_no = "hello";
//
//        // http post
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://harbour-master.mpltest/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        HarbourService service = retrofit.create(HarbourService.class);
//
////        Call<Sticker> callSync = service.createSticker(newSticker);
//        Call<StickerResponse> callAsync = service.createSticker(newSticker);
//
//        callAsync.enqueue(new Callback<StickerResponse>() {
//            @Override
//            public void onResponse(Call<StickerResponse> call, Response<StickerResponse> response) {
//                System.out.println(response.code());
//                System.out.println(response.message());
//                System.out.println(response);
//
//                if(response.isSuccessful() && response.code() == 200){
//                    Sticker sticker = response.body().sticker;
//                    System.out.println("Successfully created sticker");
//
//
//                    // print sticker
//                    try {
//                        printSticker(sticker);
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                }else{
//                    System.out.print(response.message());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<StickerResponse> call, Throwable throwable) {
//                System.out.println(throwable);
//            }
//
//        });

//        try {
//            Response<Sticker> response = callSync.execute();
//            Sticker newSticker = response.body();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }

//        System.out.println(savedSticker.execute().body());

    }


    public void populatePrinters(){

        // clear printers
        printerChoiceBox.getItems().clear();

        String[] printServicesNames = PrinterOutputStream.getListPrintServicesNames();
        for(String printServiceName: printServicesNames){
            printerChoiceBox.getItems().add(printServiceName);
        }

        for(String item: this.printerChoiceBox.getItems()){
            if(item.toLowerCase().contains("epson")){
                this.printerChoiceBox.getSelectionModel().select(item);
                break;
            }
        }

    }


    public void onPrinterRefreshButtonClick(ActionEvent actionEvent) {
        this.populatePrinters();
    }


    public BufferedImage getImage() throws IOException {
//        URL url = getURL('mpl_logo.png');
        URL url = HelloApplication.class.getResource("images/mpl_logo.png");
        return ImageIO.read(url);
    }


    public void printSticker(Sticker sticker) throws IOException {


        String selectedPrinter = (String) printerChoiceBox.getValue();
        String tinNo = sticker.getLocation_id() == 1 ? "1000719GST02" : (sticker.getLocation_id() == 2 ? "1000719GST02" : "Tin#");
        String currentDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        double gstRate = sticker.getGst_rate() * 100;
        BigDecimal gstText = new BigDecimal(gstRate).stripTrailingZeros();
        decimalFormatter.setRoundingMode(RoundingMode.HALF_UP);

        /*
         * to print one image we need to have:
         * - one BufferedImage.
         * - one bitonal algorithm to define what and how print on image.
         * - one image wrapper to determine the command set to be used on
         * image printing and how to customize it.
         */


        // specify the algorithm that defines what and how "print or not print" on each coordinate of the BufferedImage.
        // in this case, threshold 127
        Bitonal algorithm = new BitonalThreshold(127);
        // creating the EscPosImage, need buffered image and algorithm.

        BufferedImage githubBufferedImage = null;
        try {
            githubBufferedImage = getImage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        EscPosImage escposImage = new EscPosImage(new CoffeeImageImpl(githubBufferedImage), algorithm);

        // this wrapper uses esc/pos sequence: "ESC '*'"
        BitImageWrapper imageWrapper = new BitImageWrapper();


        PrintService printService = PrinterOutputStream.getPrintServiceByName(selectedPrinter);
        PrinterOutputStream printerOutputStream = null;
        try {
            printerOutputStream = new PrinterOutputStream(printService);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        EscPos escpos = new EscPos(printerOutputStream);


        // TEXT STYLES
        Style title = new Style()
//            .setFontSize(Style.FontSize._1, Style.FontSize._1)
                .setJustification(EscPosConst.Justification.Right)
                .setBold(true);

        Style subtitle = new Style(escpos.getStyle())
                .setBold(true)
                .setUnderline(Style.Underline.OneDotThick);
        Style bold = new Style(escpos.getStyle())
                .setBold(true);


        imageWrapper.setJustification(EscPosConst.Justification.Left_Default);
        escpos.write(imageWrapper, escposImage);
         escpos.writeLF(title, sticker.getLocation_name() + " Entry Pass");
        escpos.writeLF(title, "Maldives Ports Limited" );
        escpos.writeLF(title, "TIN: " + tinNo);
        escpos.feed(3)
                .writeLF("   Fee Type: " + sticker.getSticker_type_name())
                .writeLF("   Fee Amount (MVR): " + decimalFormatter.format(sticker.getAmount()))
                .writeLF("   GST ("+gstText.toString()+"%): " + decimalFormatter.format(sticker.getGst_amount()))
                .writeLF("   Total (MVR): " + decimalFormatter.format(sticker.getTotal()))
                .writeLF("   Serial: " + sticker.getSticker_no())
                .writeLF("   Printed On: " + currentDateTime);
        escpos.feed(5).cut(EscPos.CutMode.FULL);
        escpos.close();

    }


}