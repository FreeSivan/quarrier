package sivan.yue.quarrier.common.tools;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiwen.yxw on 2017/2/23.
 */
public class FileLockContainer {

    public static Map<String, Class<?>> lockMap = new HashMap<>();

    static {
        lockMap.put("segment", SegmentFileLock.class);
    }

    private static class SegmentFileLock {
    }
}
