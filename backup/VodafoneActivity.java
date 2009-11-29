package it.fritzzz.vodafoneWidget;

import it.fritzzz.utils.bean.RowModel;
import it.fritzzz.utils.wrapper.ViewWrapper;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.view.LayoutInflater;

public class VodafoneActivity extends ListActivity {
	private static final String TAG = "VodafoneActivity";
	int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
	
	
	TextView selection;
	String[] itemsId={"delete_sms","disable_notify",
					"delete_call_log"};
	
	ListView lista;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.i(TAG,"Inizializzo l'activity");
        
        /**
         * Settando il result con il valore CANCELLED, se nella configurazione non venisse
         * premuto il pulsante SALVA, il widget non verrebbe piazzato sul desktop
         */
        //setResult(RESULT_CANCELED);
        
        setContentView(R.layout.configuration);
        
        // Find the widget id from the intent. 
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        Log.i(TAG,"Widget id : "+mAppWidgetId);
        
        // If they gave us an intent without the widget id, just bail.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
        	Log.i(TAG,"Senza id");
            finish();
        }
        
//        lista = getListView();
//        lista.setOnItemClickListener(mOnClickListener);
//        
//        ArrayAdapter arr1= new ArrayAdapter<Object>(this,
//				R.layout.configuration_cb_row, R.id.label,
//				itemsCB);
//        
//        ArrayAdapter arr2= new ArrayAdapter<Object>(this,
//				R.layout.configuration_nor_row, R.id.label,
//				itemsNor);
//        
//        arr2.
//        
//        
//        setListAdapter(arr2);
        List<RowModel> list=new ArrayList<RowModel>();
        
        for (String s : itemsId) {
			list.add(new RowModel(this.getBaseContext(),s));
		}

		setListAdapter(new CheckAdapter(this, list));
		selection=(TextView)findViewById(R.id.check);
        
        

        
    }
    
    
    
    @Override
    protected void onStart() {
    	// TODO Auto-generated method stub
    	super.onStart();
    	Log.i(TAG,"Started");
    }
    
    @Override
	public void startActivity(Intent intent) {
		// TODO Auto-generated method stub
		super.startActivity(intent);
		Log.i(TAG,"StartActivity");
	}
    
    
  
    
    OnItemClickListener mOnClickListener = new OnItemClickListener() {

		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			
			Log.i(TAG,"Click on mOnClickListener");

            
			
		}
    };
    
    
	private RowModel getModel(int position) {
		return(RowModel)(((CheckAdapter)getListAdapter()).getItem(position));
	}

	public void onListItemClick(ListView parent, View v, int position, long id) {
	 	selection.setText(getModel(position).toString());
	 	//TODO : settare la preferenza nelle sharedPreferences
	}
   
	class CheckAdapter extends ArrayAdapter {
		Activity context;

		CheckAdapter(Activity context, List list) {
			super(context, R.layout.configuration_cb_row, list);

			this.context=context;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View row=convertView;
			ViewWrapper wrapper;
			CheckBox cb;

			if (row==null) {
				LayoutInflater inflater=context.getLayoutInflater();

				row=inflater.inflate(R.layout.configuration_cb_row, null);
				wrapper=new ViewWrapper(row);
				row.setTag(wrapper);
				cb=wrapper.getCheckBox();

				CompoundButton.OnCheckedChangeListener l=new CompoundButton.OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						Integer myPosition=(Integer)buttonView.getTag();
						RowModel model=getModel(myPosition);

						model.setChecked(isChecked);
						buttonView.setText(model.toString());
					}
				};

				cb.setOnCheckedChangeListener(l);
			}
			else {
				wrapper=(ViewWrapper)row.getTag();
				cb=wrapper.getCheckBox();
			}

			RowModel model=getModel(position);

			cb.setTag(new Integer(position));
			cb.setText(model.toString());
			cb.setChecked(model.isChecked());

			return(row);
		}
	}
//    /***
//     * Salvo il widget
//     */
//    Intent resultValue = new Intent();
//    resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
//    setResult(RESULT_OK, resultValue);
//    finish();
    
}
