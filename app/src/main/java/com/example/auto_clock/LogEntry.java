package com.example.auto_clock;
import java.security.PrivateKey;
import java.util.Calendar;
public class LogEntry {

    private Calendar _in;
    private Calendar _out;
    private String _longitude = null;
    private String _latitude = null;

    public LogEntry(){}

   public LogEntry(LogEntry log){
        this._in = log._in;
        this._out = log._out;
        this._latitude = log._latitude;
        this._longitude = log._longitude;

    }

    public LogEntry(Calendar in, Calendar out){
        this._in = in;
        this._out = out;
    }

    public LogEntry(Calendar in, Calendar out, String lon, String lat){
        this._longitude = lon;
        this._latitude = lat;
        this._in = in;
        this._out = out;
    }

    public Calendar get_in() {
        return _in;
    }

    public Calendar get_out() {
        return _out;
    }

    public String get_latitude() {
        return _latitude;
    }

    public String get_longitude() {
        return _longitude;
    }

    public void set_in(Calendar _in) {
        this._in = _in;
    }

    public void set_out(Calendar _out) {
        this._out = _out;
    }

    public void set_latitude(String _latitude) {
        this._latitude = _latitude;
    }

    public void set_longitude(String _longitude) {
        this._longitude = _longitude;
    }


}
