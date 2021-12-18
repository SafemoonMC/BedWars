package gg.mooncraft.minecraft.bedwars.common.utilities;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class IOUtils {
    
    public static int copy(@NotNull InputStream input, @NotNull OutputStream output) throws IOException {
        long count = copyLarge(input, output);
        return count > 2147483647L ? -1 : (int) count;
    }
    
    public static long copy(@NotNull InputStream input, @NotNull OutputStream output, int bufferSize) throws IOException {
        return copyLarge(input, output, new byte[bufferSize]);
    }
    
    public static long copyLarge(@NotNull InputStream input, @NotNull OutputStream output) throws IOException {
        return copy(input, output, 4096);
    }
    
    public static long copyLarge(@NotNull InputStream input, @NotNull OutputStream output, byte[] buffer) throws IOException {
        long count;
        int n;
        for (count = 0L; -1 != (n = input.read(buffer)); count += n) {
            output.write(buffer, 0, n);
        }
        return count;
    }
}