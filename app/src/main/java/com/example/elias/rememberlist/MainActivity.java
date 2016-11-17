package com.example.elias.rememberlist;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.support.design.widget.Snackbar;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.example.elias.rememberlist.Module.Product;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements ConfirmDeleteDialogFragment.OnPositiveListener {

    ArrayList<Product> items = new ArrayList<>();
//    ArrayAdapter<Product> adapter;
    ListView listView;
    private static final String TAG = "com.example.elias.rememberlist";
    private int itemIndex = -1;
    static ConfirmDeleteDialog dialog;
    Product lastDeletedProduct;
    int lastDeletedPosition;
    DatabaseReference firebase;
    FirebaseListAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebase = FirebaseDatabase.getInstance().getReference().child("Items");
        setAdapter();
        buttonAddClick();
        btnClearClick();
        onItemClickListener();
        btnDeleteSelected();
    }


    //This method is called before our activity is created
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //ALWAYS CALL THE SUPER METHOD
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState");
		/* Here we put code now to save the state */
        outState.putParcelableArrayList("savedList", items);

    }

    //this is called when our activity is recreated, but
    //AFTER our onCreate method has been called
    //EXTREMELY IMPORTANT DETAIL
    @Override
    protected void onRestoreInstanceState(Bundle savedState) {
        //MOST UI elements will automatically store the information
        //if we call the super.onRestoreInstaceState
        //but other data will be lost.
        super.onRestoreInstanceState(savedState);
        Log.i(TAG, "onRestoreInstanceState");
		/*Here we restore any state */
        //TextView savedName = (TextView) findViewById(R.id.name);
        //in the line below, notice key value matches the key from onSaved
        //this is of course EXTREMELY IMPORTANT
        this.items = savedState.getParcelableArrayList("savedList");

        //since this method is called AFTER onCreate
        //we need to set the text field
        //try to comment the line below out and
        //see the effect after orientation change (after saving some name)
        setAdapter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //We we set that we want to use the xml file
        //under the menu directory in the resources and
        // that we want to use the specific file called "main.xml"
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                Toast.makeText(this, "Application icon clicked!",
                        Toast.LENGTH_SHORT).show();
                return true; //return true, means we have handled the event
            case R.id.item_about:
                Toast.makeText(this, "About item clicked!", Toast.LENGTH_SHORT)
                        .show();
                return true;
            case R.id.item_delete:
                Toast.makeText(this, "Delete item clicked!", Toast.LENGTH_SHORT)
                        .show();
                return true;
            case R.id.item_help:
                Toast.makeText(this, "Help item clicked!", Toast.LENGTH_SHORT)
                        .show();
                return true;
            case R.id.item_refresh:
                Toast.makeText(this, "Refresh item clicked!", Toast.LENGTH_SHORT)
                        .show();
                return true;
        }

        return false; //we did not handle the event
    }

    public void showSnackbar(){
        final View parent = listView;
        Snackbar snackbar = Snackbar
                .make(parent, "Product deleted!", Snackbar.LENGTH_LONG)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //This code will ONLY be executed in case that
                        //the user has hit the UNDO button
                        items.add(lastDeletedPosition, lastDeletedProduct);
                        getMyAdapter().notifyDataSetChanged();
                        Snackbar snackbar = Snackbar.make(parent, "Product not deleted!", Snackbar.LENGTH_SHORT);
                        //Show the user we have restored the name - but here
                        //on this snackbar there is NO UNDO - so no SetAction method is called
                        //if you wanted, you could include a REDO on the second action button
                        //for instance.
                        snackbar.show();
                    }
                });
        snackbar.show();
    }

    public void saveCopy(){
        lastDeletedPosition = listView.getCheckedItemPosition();
        lastDeletedProduct = items.get(lastDeletedPosition);
    }


    public void setAdapter(){
        mAdapter = new FirebaseListAdapter<Product>(this, Product.class, android.R.layout.simple_list_item_checked, firebase) {
            @Override
            protected void populateView(View view, Product product, int position) {
                ((TextView)view.findViewById(android.R.id.text1)).setText(product.toString());
            }
        };

        listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(mAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    public FirebaseListAdapter getMyAdapter()
    {
        return mAdapter;
    }

    protected void buttonAddClick(){
        Button addButton = (Button) findViewById(R.id.btn_add);
        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText itemText = (EditText) findViewById(R.id.item_Name);
                EditText itemQuantity = (EditText) findViewById(R.id.item_sum);
                String itemName = itemText.getText().toString();
                int itemQuant = Integer.parseInt(itemQuantity.getText().toString());
                Product product = new Product(itemName, itemQuant);

//                items.add(product);
                firebase.push().setValue(product);
                //The next line is needed in order to say to the ListView
                //that the data has changed - we have added stuff now!
                getMyAdapter().notifyDataSetChanged();
                itemText.setText("");
                itemQuantity.setText("");
            }
        });
    }

    protected void btnClearClick(){
        Button clearBtn = (Button) findViewById(R.id.btn_clear);

        clearBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(itemIndex == -1){
//                    Toast toast =
                }else{
                    dialog = new ConfirmDeleteDialog();
                    dialog.show(getFragmentManager(), "confirmDialog");
                }
            }
        });
    }

    protected void onItemClickListener(){
        listView = (ListView) findViewById(R.id.list_view);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemIndex = position;
            }
        });
    }

    protected void btnDeleteSelected(){
        Button deleteBtn = (Button) findViewById(R.id.btn_delete);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCopy();
                items.remove(lastDeletedProduct);
                getMyAdapter().notifyDataSetChanged();
                itemIndex = -1;
                showSnackbar();
            }
        });
    }

    @Override
    public void onPositiveClicked() {
        getMyAdapter().cleanup();
        itemIndex = -1;
    }

    public static class ConfirmDeleteDialog extends ConfirmDeleteDialogFragment{

        @Override
        protected void negativeClick(){

        }
    }
}
