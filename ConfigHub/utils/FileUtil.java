package de.aggromc.confighubhost.utils;

import java.util.*;
import java.nio.file.*;
import java.io.*;

public class FileUtil
{
    private static final int BUFFER_SIZE = 8192;
    
    public static boolean copy(final InputStream in, final Path target, final CopyOption... options) throws IOException {
        Objects.requireNonNull(in);
        boolean replaceExisting = false;
        final int length = options.length;
        int i = 0;
        while (i < length) {
            final CopyOption opt = options[i];
            if (opt == StandardCopyOption.REPLACE_EXISTING) {
                replaceExisting = true;
                ++i;
            }
            else {
                if (opt == null) {
                    throw new NullPointerException("options contains 'null'");
                }
                throw new UnsupportedOperationException(opt + " not supported");
            }
        }
        SecurityException se = null;
        if (replaceExisting) {
            try {
                Files.deleteIfExists(target);
            }
            catch (SecurityException x) {
                se = x;
            }
        }
        try {
            final OutputStream ostream = Files.newOutputStream(target, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
        }
        catch (FileAlreadyExistsException x2) {
            if (se != null) {
                throw se;
            }
            throw x2;
        }
        OutputStream ostream;
        try (final OutputStream out = ostream) {
            return copy(in, out);
        }
    }
    
    private static boolean copy(final InputStream source, final OutputStream sink) throws IOException {
        long nread = 0L;
        final byte[] buf = new byte[8192];
        int n;
        while ((n = source.read(buf)) > 0) {
            sink.write(buf, 0, n);
            nread += n;
            if (nread > 20971520L) {
                source.close();
                sink.close();
                return false;
            }
        }
        return true;
    }
}
