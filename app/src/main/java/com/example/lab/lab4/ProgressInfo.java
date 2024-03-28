package com.example.lab.lab4;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class ProgressInfo implements Parcelable {
    private int downloadedAmount;
    private int totalSize;
    private ProgressStatus status;

    public ProgressInfo(int downloadedAmount, int totalSize, ProgressStatus status) {
        this.downloadedAmount = downloadedAmount;
        this.totalSize = totalSize;
        this.status = status;
    }

    public ProgressInfo(Parcel bundle) {
        this.downloadedAmount = bundle.readInt();
        this.totalSize = bundle.readInt();
        this.status = ProgressStatus.valueOf(bundle.readString());
    }

    public static final Creator<ProgressInfo> CREATOR = new Creator<ProgressInfo>() {
        @Override
        public ProgressInfo createFromParcel(Parcel in) {
            return new ProgressInfo(in);
        }

        @Override
        public ProgressInfo[] newArray(int size) {
            return new ProgressInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(this.downloadedAmount);
        dest.writeInt(this.totalSize);
        dest.writeString(this.status.name());
    }

    public int getDownloadedAmount() {
        return downloadedAmount;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public ProgressStatus getStatus() {
        return status;
    }
}
