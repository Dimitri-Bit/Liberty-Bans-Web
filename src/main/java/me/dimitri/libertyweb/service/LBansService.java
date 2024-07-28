package me.dimitri.libertyweb.service;

import space.arim.libertybans.api.LibertyBans;
import space.arim.libertybans.bootstrap.BaseFoundation;

public interface LBansService {

    boolean setup();
    LibertyBans getLibertyBans();
    BaseFoundation getBaseFoundation();

}
