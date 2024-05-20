package ecrlib_globalpay;

public class ecrlib {

    public static native  Data ReqBin(int inUserTimeOut);//bin request
    public static native  Data ReqBinOffSale(int inUserTimeOut,int isInstallment,String numMonths);//bin request offlene
    public static native  Data DoSale(int inUserTimeOut,String Amount);// sale request
    public static native  Data DoOffSale(int inUserTimeOut,String Amount, String ApproveCode,String numMonths);//offline sale request
    public static native  Data DoVoidSale(int inUserTimeOut,String hostID,String invNumber,String tenure,String isInstallmentVoid);
    public static native  Data DoRecovery(int inUserTimeOut,String InvoiceNumber);// recover old transaction data
    public static native  Data ReqBinInstallment(int inUserTimeOut,	String numMonths);
    public static native  Data DoInstallment(int inUserTimeOut,String amount,String numMonth);
    public static native  Data ReqRedeemBin(int inUserTimeOut);
    public static native  Data DoRedeem(int inUserTimeOut,String amount);
    public static native  Data ReqEnqBin(int inUserTimeOut);
    public static native  Data DoEnquiry(int inUserTimeOut);
    public static native  Data ReqAuthBin(int inUserTimeOut);
    public static native  Data DoAuth(int inUserTimeOut,String amount);
    public static native  void BeepIt(int freq,int duration);

    static int timeout = 6;
}