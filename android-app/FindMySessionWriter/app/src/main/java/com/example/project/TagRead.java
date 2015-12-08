package com.example.wlanhelper;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Parcelable;

public class TagRead {

    private MessageDisplayer messageDisplayer;
    public static final int NEWCONFPRIORITY = 40;

    public TagRead(MessageDisplayer messageDisplayer) {
        this.messageDisplayer = messageDisplayer;
    }

    /**
     * Parses the wifi configuration details from the NFC tag.
     * 
     * @param intent  Intent that contains the NFC tag info.
     * @return An info object containing the wifi configuration details.
     * @throws ReadException
     */
    public WifiInfo parseWifiInfoFromTag(Intent intent) throws ReadException {

        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if (rawMsgs == null) {
            throw new ReadException("Intent did not include the parcelable for the NDEF messages.");
        }

        NdefMessage msg = (NdefMessage) rawMsgs[0];
        NdefRecord wifiInfoRecord = msg.getRecords()[0];
        String wifiInfoStr = new String(wifiInfoRecord.getPayload());
        return doParse(wifiInfoStr);
    }

    public WifiInfo doParse(String wifiInfoStr) throws ReadException {
        // validate the format of the data
        int indexOfSeparator = wifiInfoStr.indexOf(Constants.WIFIINFO_SEPARATOR);
        if (indexOfSeparator != -1) {
            String ssid = new String(wifiInfoStr.substring(0, indexOfSeparator));
            String preSharedKey = new String(wifiInfoStr.substring(indexOfSeparator + 1));

            messageDisplayer.displayMessage("");
            return new WifiInfo(ssid, preSharedKey);

        } else {
            throw new ReadException("The expected separator char not found in the tag data");
        }
    }

}