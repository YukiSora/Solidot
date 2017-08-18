package moe.yukisora.solidot.modles;

import java.io.Serializable;

public class ArticleData implements Serializable {
    public int sid;
    public String title;
    public String datetime;
    public String reference;
    public String article;
    public int numberOfView;

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ArticleData && sid == ((ArticleData)obj).sid;
    }

    @Override
    public int hashCode() {
        return sid;
    }
}
