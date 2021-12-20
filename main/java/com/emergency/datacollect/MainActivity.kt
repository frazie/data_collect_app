package com.emergency.datacollect

import android.content.Context
import android.content.DialogInterface
import android.database.sqlite.SQLiteDatabase
import android.icu.text.CaseMap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    var edtName:EditText? = null
    var edtEmail:EditText? = null
    var edtID:EditText? = null
    var edtPhone:EditText? = null
    var save:Button? = null
    var delet:Button? = null
    var ona:Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        save = findViewById(R.id.btnSave)
        delet = findViewById(R.id.btnDelete)
        ona = findViewById(R.id.btnView)
        edtName = findViewById(R.id.edtName)
        edtEmail = findViewById(R.id.edtEmail)
        edtID = findViewById(R.id.edtID)
        edtPhone = findViewById(R.id.edtNum)


        //create the data base

        var db:SQLiteDatabase = openOrCreateDatabase("firefoxSystemDB",Context.MODE_PRIVATE,null)
        //create table inside of the database
        db.execSQL("CREATE TABLE IF NOT EXISTS users(jina VARCHAR, emmy VARCHAR, kitambulisho VARCHAR, simu VARCHAR) ")

        save!!.setOnClickListener {
            // start receiving the data to be saved from the user
            var name = edtName!!.text.toString().trim()
            var email = edtEmail!!.text.toString().trim()
            var idnumber = edtID!!.text.toString().trim()
            var phonenumber = edtPhone!!.text.toString().trim()
                //check if teh user is attempting to save empty fields
            if (name.isEmpty() || email.isEmpty() || idnumber.isEmpty() || phonenumber.isEmpty()){
                display_message("Empty Fields","Please fill all the inputs")
            }else{
                //proceed to save you data into the database
                db.execSQL("INSERT INTO users VALUES('"+name+"','"+email+"','"+idnumber+"','"+phonenumber+"')")
                display_message("Success",",users saved succesfully")
                clear()
            }

        }

        delet!!.setOnClickListener {
        //to delete take the user's n ID number and use it as a unique identifier
            var idNum = edtID!!.text.toString().trim()
            //check if user is submitting empty fields
            if (idNum.isEmpty()){
                display_message("EMPTY FIELD","Please fill this field")
            }else{
                //use the cursor to select the user with the given ID
                var cursor = db.rawQuery("SELECT * FROM users WHERE kitambulisho='"+idNum+"'",null)
                //check if the record is available in the DB
                if (cursor.count == 0){
                    display_message("NO RECORD","sorry, we did not find that user")
                }else{
                    //finally delete the record
                    db.execSQL("DELETE FROM users WHERE kitambulisho='"+idNum+"'")
                    display_message("SUCCESS","Record deleted successfully")
                    clear()
                }
            }
        }

        ona!!.setOnClickListener {
            // use cursor to select the data from the database
            var cursor = db.rawQuery("SELECT * FROM users",null)
            //check if there's any record in the db
            if (cursor.count == 0){
                display_message("EMPTY DB","sorry, we found no records")
            }else{
                var buffer = StringBuffer()
                while (cursor.moveToNext()){
                    buffer.append(cursor.getString(0)+"\n")
                    buffer.append(cursor.getString(1)+"\n")
                    buffer.append(cursor.getString(2)+"\n\n")
                }
                //use the display_message() to display the records
                display_message("USERS",buffer.toString())
            }
        }

    }
    fun clear(){
        edtEmail!!.setText(null)
        edtName!!.setText(null)
        edtID!!.setText(null)
        edtPhone!!.setText(null)
    }

    // this function displays all the error messages
    fun display_message(title: String, message:String){
        var alertDialog:AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialog.setCancelable(false)
        alertDialog.setTitle(title)
        alertDialog.setMessage(message)
        alertDialog.setPositiveButton("OK",DialogInterface.OnClickListener{
                dialogInterface, i ->
            dialogInterface.dismiss()
        })
        alertDialog.create().show()

    }

}