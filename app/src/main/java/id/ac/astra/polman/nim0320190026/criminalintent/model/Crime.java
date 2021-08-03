package id.ac.astra.polman.nim0320190026.criminalintent.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.UUID;

public class Crime {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("date")
    @Expose
    private Date date;

    @SerializedName("solved")
    @Expose
    private boolean solved;

    @SerializedName("suspect")
    @Expose
    private String suspect;

    @SerializedName("suspectNumber")
    @Expose
    private String suspectNumber;


    public Crime() {
        id = UUID.randomUUID().toString();
        title = "";
        date = new Date();
        solved = false;
    }

    public Crime(String id, String title, Date date, boolean solved, String suspect, String suspectNumber) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.solved = solved;
        this.suspect = suspect;
        this.suspectNumber = suspectNumber;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    public String getSuspect() {
        return suspect;
    }

    public void setSuspect(String suspect) {
        this.suspect = suspect;
    }

    public String getSuspectNumber() {
        return suspectNumber;
    }

    public void setSuspectNumber(String suspectNumber) {
        this.suspectNumber = suspectNumber;
    }

    public String getPhotoFilename() {
        return "IMG_" + getId() + ".jpg";
    }

    @Override
    public String toString() {
        return "Crime{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", date=" + date +
                ", solved=" + solved +
                ", suspect='" + suspect + '\'' +
                ", suspectNumber='" + suspectNumber + '\'' +
                '}';
    }
}
