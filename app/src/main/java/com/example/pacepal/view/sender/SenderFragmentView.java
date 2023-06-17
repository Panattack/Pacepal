package com.example.pacepal.view.sender;

import java.util.ArrayList;

public interface SenderFragmentView {
    public void showFiles(ArrayList<String> files);
    public void popMsg(String message);
    public void alertBox(String title, String message);
    public ArrayList<String> submitClicked();
}
