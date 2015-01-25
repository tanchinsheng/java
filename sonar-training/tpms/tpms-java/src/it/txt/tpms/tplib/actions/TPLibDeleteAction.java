package it.txt.tpms.tplib.actions;

public class TPLibDeleteAction extends TPLibAction{

	public String getActionName() {
		return "tp_lib_delete";
	}

	public String getNewStatus() {
		return "Obsolete";
	}

	public String getInitialStatus() {		
		return "In_Production";
	}

	public String getLabel() {
		return "TPLibDeleteAction";
	}
}
