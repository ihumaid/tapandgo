package mv.port.harbour_tapngo;

import com.google.gson.annotations.SerializedName;

public class StickerResponse {

    @SerializedName("success")
    public boolean success;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public Sticker sticker;


}
