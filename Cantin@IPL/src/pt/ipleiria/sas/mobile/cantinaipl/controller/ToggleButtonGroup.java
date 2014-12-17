package pt.ipleiria.sas.mobile.cantinaipl.controller;

import java.util.LinkedList;
import android.app.Activity;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

public class ToggleButtonGroup{
	
	public interface GrupoListener {
		void grupoChanged(boolean []values);
	}
	
	private GrupoListener listener;
	private LinkedList<ToggleButton> botoes;

	public ToggleButtonGroup(Activity a, int... ids) {
		botoes = new LinkedList<ToggleButton>();
		
		for (int id : ids) {
			ToggleButton botao = (ToggleButton) a.findViewById(id);	
			botoes.add(botao);
			
			botao.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (listener!=null) {
						listener.grupoChanged(getCheckedValues());
					}
				}
			});
		}
	}
	
	public void setGroupListener(GrupoListener listener) {
		this.listener=listener;
	}
	
	public boolean[] getCheckedValues() {
		boolean []valores=new boolean[botoes.size()];
		int i=0;
		for (ToggleButton b : botoes)
			valores[i++]=b.isChecked();
		return valores;
	}
}