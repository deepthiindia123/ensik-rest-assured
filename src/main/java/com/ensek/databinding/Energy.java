package com.ensek.databinding;

public class Energy {
    private EnergyHelper electric;
    private EnergyHelper gas;
    private EnergyHelper nuclear;
    private EnergyHelper oil;

    public EnergyHelper getElectric() {
        return electric;
    }

    public EnergyHelper getGas() {
        return gas;
    }

    public EnergyHelper getNuclear() {
        return nuclear;
    }

    public EnergyHelper getOil() {
        return oil;
    }
}
