package mv.port.harbour_tapngo;


public class Sticker {

    private String sticker_no;
    private String sticker_type_name;
    private String location_name;
    private int location_id;
    private String gate;
    private int sticker_type;
    private double amount;
    private double gst_amount;
    private double gst_rate;
    private double total;
    private String source;
    private int payment_type;
    private String terminal_id;
    private String reference_no;
    private String trace_no;
    private String card_type;
    private String card_no;
    private String batch_no;

    public Sticker(Integer location_id, String gate, Integer sticker_type, String source, Integer payment_type) {
        this.location_id = location_id;
        this.gate = gate;
        this.sticker_type = sticker_type;
        this.source = source;
        this.payment_type = payment_type;
    }

    public String getSticker_no() {
        return this.sticker_no;
    }

    public void setSticker_no(String sticker_no) {
        this.sticker_no = sticker_no;
    }

    public String getSticker_type_name() {
        return this.sticker_type_name;
    }

    public void setSticker_type_name(String sticker_type_name) {
        this.sticker_type_name = sticker_type_name;
    }

    public String getLocation_name() {
        return this.location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public Integer getLocation_id() {
        return this.location_id;
    }

    public void setLocation_id(Integer location_id) {
        this.location_id = location_id;
    }

    public String getGate() {
        return gate;
    }

    public void setGate(String gate) {
        this.gate = gate;
    }

    public Integer getSticker_type() {
        return this.sticker_type;
    }

    public void setSticker_type(Integer sticker_type) {
        this.sticker_type = sticker_type;
    }

    public double getAmount() {
        return this.amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getGst_amount() {
        return this.gst_amount;
    }

    public void setGst_amount(double gst_amount) {
        this.gst_amount = gst_amount;
    }

    public double getGst_rate() {
        return this.gst_rate;
    }

    public void setGst_rate(double gst_rate) {
        this.gst_rate = gst_rate;
    }

    public double getTotal() {
        return this.total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Integer getPayment_type() {
        return this.payment_type;
    }

    public void setPayment_type(Integer payment_type) {
        this.payment_type = payment_type;
    }

    public String getTerminal_id() {
        return terminal_id;
    }

    public void setTerminal_id(String terminal_id) {
        this.terminal_id = terminal_id;
    }

    public String getTrace_no() {
        return trace_no;
    }

    public void setTrace_no(String trace_no) {
        this.trace_no = trace_no;
    }

    public String getReference_no() {
        return this.reference_no;
    }

    public void setReference_no(String reference_no) {
        this.reference_no = reference_no;
    }

    public String getCard_type() {
        return card_type;
    }

    public void setCard_type(String card_type) {
        this.card_type = card_type;
    }

    public String getCard_no() {
        return card_no;
    }

    public void setCard_no(String card_no) {
        this.card_no = card_no;
    }

    public String getBatch_no() {
        return batch_no;
    }

    public void setBatch_no(String batch_no) {
        this.batch_no = batch_no;
    }

}
