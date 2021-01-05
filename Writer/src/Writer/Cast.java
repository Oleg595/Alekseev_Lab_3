package Writer;

import ru.spbstu.pipeline.TYPE;

import java.nio.ByteBuffer;

public class Cast {
    static byte[] ShortToByte(short[] short_massive){
        byte[] data = new byte[short_massive.length * 2];
        for(int i = 0; i < short_massive.length; i++){
            data[2 * i + 1] = (byte)(short_massive[i] & 255);
            data[2 * i] = (byte)(short_massive[i] >> 8 & 255);
        }
        return data;
    }

    static byte[] CharToByte(char[] char_massive){
        byte[] data = new byte[char_massive.length];
        for(int i = 0; i < char_massive.length; i++){
            data[i] = (byte)char_massive[i];
        }
        return data;
    }

    static short[] ByteToShort(byte[] byte_massive){
        short[] massive = new short[byte_massive.length / 2];
        ByteBuffer buffer = ByteBuffer.wrap(byte_massive);
        for(int i = 0; i < massive.length; i++){
            massive[i] = buffer.getShort();
        }
        return massive;
    }

    static char[] ByteToChar(byte[] byte_massive){
        char[] massive = new char[byte_massive.length];
        for(int i = 0; i < byte_massive.length; i++){
            massive[i] = (char)byte_massive[i];
        }
        return massive;
    }

    static byte[] Copy(byte[] byte_massive){
        byte[] massive = new byte[byte_massive.length];
        System.arraycopy(byte_massive, 0, massive, 0, byte_massive.length);
        return massive;
    }

    static TYPE Input_Type(TYPE[] massive1, TYPE[] massive2){
        for(int i = 0; i < massive1.length; i++){
            for(int j = 0; j < massive2.length; j++){
                if(massive1[i] == massive2[j]){
                    return massive1[i];
                }
            }
        }
        return null;
    }
}
