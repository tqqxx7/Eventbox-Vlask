package services;

public class APIUtils {
    public static final String base_Url = "https://sukien.vanlanguni.edu.vn/mobile/";



    //Gui va nhan du lieu chua trong interface DataClient tu base_url
    public static DataClient getData(){
      return RetrofitClient.getClient(base_Url).create(DataClient.class);
    };
}
