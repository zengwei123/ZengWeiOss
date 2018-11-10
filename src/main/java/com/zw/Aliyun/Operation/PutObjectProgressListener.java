package com.zw.Aliyun.Operation;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.event.ProgressEvent;
import com.aliyun.oss.event.ProgressEventType;
import com.aliyun.oss.event.ProgressListener;
import com.aliyun.oss.model.PutObjectRequest;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;

public class PutObjectProgressListener implements ProgressListener {
    private long bytesWritten = 0;
    private long totalBytes = -1;
    private boolean succeed = false;
    public static long size=0;
    private static final DecimalFormat df   = new DecimalFormat("######0.00");
    @Override
    public void progressChanged(ProgressEvent progressEvent) {
         try {
            long bytes = progressEvent.getBytes();
            ProgressEventType eventType = progressEvent.getEventType();
            switch (eventType) {
                case TRANSFER_STARTED_EVENT:
                    OssWebSocket.sendMessage("任务开始");
                    break;
                case REQUEST_BYTE_TRANSFER_EVENT:
                    this.bytesWritten += bytes;
                    OssWebSocket.sendMessage(df.format(((double)this.bytesWritten/(double)size)*100.00)+"%");
                    break;
                case TRANSFER_COMPLETED_EVENT:
                    this.succeed = true;
                    OssWebSocket.sendMessage("成功");
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isSucceed() {
        return succeed;
    }
}