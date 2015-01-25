package it.txt.tpms.tplib.actions;


public class TPLibProductionAction extends TPLibAction{
	public String getActionName() {
		return "tp_lib_production";
	}

	public String getNewStatus() {
		return "In_Production";
	}

	public String getInitialStatus() {
		return "Sent2TPLib";
	}

	public String getLabel() {
		return "TPLibProductionAction";
	}
}
