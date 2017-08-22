package moe.yukisora.solidot.modles;

import android.os.Parcel;
import android.os.Parcelable;

public class ArticleData implements Parcelable {
    public int sid;
    public String title;
    public String datetime;
    public String reference;
    public String content;

    public ArticleData() {
    }

    private ArticleData(Parcel parcel) {
        sid = parcel.readInt();
        title = parcel.readString();
        datetime = parcel.readString();
        reference = parcel.readString();
        content = parcel.readString();
    }

    public static final Creator<ArticleData> CREATOR = new Creator<ArticleData>() {
        @Override
        public ArticleData createFromParcel(Parcel parcel) {
            return new ArticleData(parcel);
        }

        @Override
        public ArticleData[] newArray(int i) {
            return new ArticleData[i];
        }
    };


    @Override
    public int describeContents() {
        return sid;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(sid);
        parcel.writeString(title);
        parcel.writeString(datetime);
        parcel.writeString(reference);
        parcel.writeString(content);
    }

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
                "    content: \"" + content + "\",\n" +
                "}";
    }
}
