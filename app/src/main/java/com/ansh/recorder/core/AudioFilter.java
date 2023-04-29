package com.ansh.recorder.core;

import java.io.File;
import java.io.FileFilter;

public class AudioFilter implements FileFilter {

    // only want to see the following audio file types
    private final String[] extensionCllRcc = {".aac", ".mp3", ".wav", ".ogg", ".midi", ".3gp", ".mp4", ".m4a", ".amr", ".flac"};

    @Override
    public boolean accept(File pathnameCllRcc) {

        // if we are looking at a directory/file that's not hidden we want to see it so return TRUE
        if (pathnameCllRcc.isDirectory() && !pathnameCllRcc.isHidden()) {
            return true;
        }

        // loops through and determines the extension of all files in the directory
        // returns TRUE to only show the audio files defined in the String[] extension array
        for (String extCllRcc : extensionCllRcc) {
            if (pathnameCllRcc.getName().toLowerCase().endsWith(extCllRcc)) {
                return true;
            }
        }
        return false;
    }
}
