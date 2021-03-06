package com.pointrest.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class DialogPreferitiFragment extends DialogFragment {

	public static DialogPreferitiFragment getInstance(long id){
		DialogPreferitiFragment vItem = new DialogPreferitiFragment(id);
		Bundle bundle = new Bundle();
	    vItem.setArguments(bundle);
		return vItem;
	}
	
	private long tmpID;
	
	private DialogPreferitiFragment(long id){
		tmpID= id;
	}
	
	@Override
	public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		
		//Bundle bundle = getArguments();
		
		builder.setTitle("Rimozione preferito");
		
		
		builder.setMessage("Rimuovere il preferito?");
		
		builder.setPositiveButton("Rimuovi", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent preferito = new Intent();
				preferito.putExtra("ID", tmpID);
				
				getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, preferito);				
			}
		}); 
		
		builder.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, tipologia);
			}
		});
		return builder.create();
	}
}
