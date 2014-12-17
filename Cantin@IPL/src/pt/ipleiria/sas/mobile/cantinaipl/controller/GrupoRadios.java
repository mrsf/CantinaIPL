package pt.ipleiria.sas.mobile.cantinaipl.controller;

import java.util.LinkedList;

import pt.ipleiria.sas.mobile.cantinaipl.R;

import android.app.Activity;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class GrupoRadios implements OnCheckedChangeListener {

	private LinkedList<RadioButton> botoes;
	private EditText ediTxtOutro;
	private RadioButton rdOutro;
	Activity activity;

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

		if (buttonView == rdOutro) {
			if (isChecked) {
				ediTxtOutro.setHint("");
				ediTxtOutro.setFocusable(true);
				ediTxtOutro.setFocusableInTouchMode(true);
				ediTxtOutro.requestFocus();

				/*
				 * Deveria chamar o teclado
				 * ediTxtOutro.setOnFocusChangeListener(new
				 * View.OnFocusChangeListener() {
				 * 
				 * @Override public void onFocusChange(View v, boolean hasFocus)
				 * { ediTxtOutro.post(new Runnable() {
				 * 
				 * @Override public void run() { InputMethodManager imm =
				 * (InputMethodManager)
				 * activity.getSystemService(Context.INPUT_METHOD_SERVICE);
				 * imm.showSoftInput(ediTxtOutro,
				 * InputMethodManager.SHOW_IMPLICIT); } }); } });
				 */
			} else {
				ediTxtOutro.setHint("outro");
				ediTxtOutro.setFocusable(false);
				ediTxtOutro.setFocusableInTouchMode(false);
			}
		}

		if (isChecked) {
			for (RadioButton r : botoes) {
				if (r != buttonView)
					r.setChecked(!isChecked);
				else
					r.setChecked(isChecked);
			}
		}
	}

	public GrupoRadios(Activity c, int... ids) {
		botoes = new LinkedList<RadioButton>();
		ediTxtOutro = (EditText) c.findViewById(R.id.editTextOutroConta);
		rdOutro = (RadioButton) c.findViewById(R.id.radioButtonOutroConta);
		activity = c;

		for (int id : ids) {
			RadioButton r = (RadioButton) c.findViewById(id);
			botoes.add(r);
			r.setOnCheckedChangeListener(this);
		}
			
	/*
			r.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					if (isChecked) {
						for (RadioButton r : botoes) {
							if (r != buttonView)
								r.setChecked(!isChecked);
							else
								r.setChecked(isChecked);

						}
					}
				}
			});
		}
		*/
	}

	public int getCheckedRadio() {
		for (RadioButton r : botoes)
			if (r.isChecked())
				return r.getId();
		return -1;
	}

}
