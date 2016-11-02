package moe.yukisora.solidot;

import java.io.Serializable;

public class NewsData implements Serializable {
    public int sid;
    public String title;
    public String date;
    public String reference;
    public String article;
    public int numberOfView;
}
