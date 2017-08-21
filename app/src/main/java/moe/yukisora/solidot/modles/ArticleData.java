package moe.yukisora.solidot.modles;

import java.io.Serializable;

public class ArticleData implements Serializable {
    public int sid;
    public String title;
    public String datetime;
    public String reference;
    public String article;

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ArticleData && sid == ((ArticleData)obj).sid;
    }

    @Override
    public int hashCode() {
        return sid;
    }

    @Override
    public String toString() {
        return "ArticleData {\n" +
                "    sid: " + sid + ",\n" +
                "    title: \"" + title + "\",\n" +
                "    datetime: \"" + datetime + "\",\n" +
                "    reference: \"" + reference + "\",\n" +
                "    article: \"" + article + "\",\n" +
                "}";
    }
}
