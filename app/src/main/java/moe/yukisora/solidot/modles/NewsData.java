package moe.yukisora.solidot.modles;

import java.io.Serializable;

public class NewsData implements Serializable {
    public int sid;
    public String title;
    public String date;
    public String reference;
    public String article;
    public int numberOfView;

    @Override
    public boolean equals(Object obj) {
        return obj instanceof NewsData && sid == ((NewsData)obj).sid;
    }

    @Override
    public int hashCode() {
        return sid;
    }
}
