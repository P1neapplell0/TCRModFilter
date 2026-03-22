package com.p1nero.tcrmodfilter.service;

import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.loading.ImmediateWindowHandler;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import settingdust.preloading_tricks.api.PreloadingEntrypoint;
import settingdust.preloading_tricks.api.PreloadingTricksCallbacks;

import java.util.Set;

public class TCRPreloadingEntrypoint implements PreloadingEntrypoint {

    private static boolean acceleratedRenderingRemoved;
    private static boolean yesSteveModelRemoved;
    private static boolean acceleratedRecollidingRemoved;

    public static final Logger LOGGER = LogUtils.getLogger();

    public TCRPreloadingEntrypoint() {
        PreloadingTricksCallbacks.SETUP_MODS.register(modManager -> {
            String glVersion = ImmediateWindowHandler.getGLVersion();
            String[] parts = glVersion.split("\\.");
            int major;
            int minor;
            major = Integer.parseInt(parts[0]);
            minor = Integer.parseInt(parts[1]);
            if(major < 4 || (major == 4 && minor < 6)) {
                if(modManager.removeById("acceleratedrendering")){
                    LOGGER.warn("GLVersion < 4.6, acceleratedrendering mod has been removed.");
                    acceleratedRenderingRemoved = true;
                }
            }
            String arch = System.getProperty("sun.arch.data.model");
            if((SystemUtils.IS_OS_MAC || !arch.equals("64"))) {
                if(modManager.removeById("acceleratedrecoiling")) {
                    LOGGER.warn("On MacOS or 32-bit arch, acceleratedrecoiling mod has been removed.");
                    acceleratedRecollidingRemoved = true;
                }
            }
            if(modManager.getById("epicfight") != null){
                if(modManager.removeById("yes_steve_model")) {
                    LOGGER.warn("Incompatible with epic fight, yes_steve_model mod has been removed.");
                    yesSteveModelRemoved = true;
                }
            }
            if(modManager.getById("tcrcore") != null) {
                modManager.removeByIds(Set.of("projecte"));
            }
        });
    }

    public static boolean isAcceleratedRecollidingRemoved() {
        return acceleratedRecollidingRemoved;
    }

    public static boolean isAcceleratedRenderingRemoved() {
        return acceleratedRenderingRemoved;
    }

    public static boolean isYesSteveModelRemoved() {
        return yesSteveModelRemoved;
    }

}
