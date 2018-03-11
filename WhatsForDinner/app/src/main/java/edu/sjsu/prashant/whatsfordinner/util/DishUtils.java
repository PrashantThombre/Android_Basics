package edu.sjsu.prashant.whatsfordinner.util;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import edu.sjsu.prashant.whatsfordinner.model.NewDish;

/**
 *
 * Created by Prashant Thombre on 3/6/2018.
 *
 */

public class DishUtils {
    private static final String TAG = DishUtils.class.getSimpleName();


    public List<NewDish> readDishList(Context context, String file_name){
        List<NewDish> newDishList = new ArrayList<>();
        FileInputStream ifile;
        ObjectInputStream in;
        try {
            Log.i(TAG, "readDishList: Absolute File Name - "+ context.getFilesDir().getPath()+file_name);
            Log.d(TAG, "readDishList: Starting deserialization operation");
            ifile = new FileInputStream(context.getFilesDir().getPath()+file_name);
            in = new ObjectInputStream(ifile);
            newDishList = (List<NewDish>) in.readObject();
            in.close();
            Log.d(TAG, "readDishList: Completed deserialization operation");
        } catch (FileNotFoundException fnf_ex) {
            Log.e(TAG, "readDishList: FileNotFoundException in deserialization operation");
            fnf_ex.printStackTrace();
        } catch (IOException io_ex){
            Log.e(TAG, "readDishList: IOError in deserialization operation");
            io_ex.printStackTrace();
        } catch (ClassNotFoundException cnf_ex){
            Log.e(TAG, "readDishList: ClassNotFoundException in deserialization operation");
            cnf_ex.printStackTrace();
        } catch (Exception ex){
            Log.e(TAG, "readDishList: Exception in deserialization operation");
            ex.printStackTrace();
        }
        return newDishList;
    }

    public void writeDishList(Context context, String file_name, List<NewDish> newDishList){
        FileOutputStream ofile;
        ObjectOutputStream out;
        try {
            Log.d(TAG, "writeDishList: Starting serialization operation");
            ofile = new FileOutputStream(context.getFilesDir().getPath()+file_name);
            out = new ObjectOutputStream(ofile);
            out.writeObject(newDishList);
            out.close();
            Log.d(TAG, "writeDishList: Completed serialization operation");
        } catch (Exception ex) {
            Log.e(TAG, "writeDishList: Error in serialization operation");
            ex.printStackTrace();
        }
    }
}
