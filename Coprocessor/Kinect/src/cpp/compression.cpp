#include <lz4.h>

using namespace cv;

static const LZ4F_preferences_t lz4_preferences = {
	{ LZ4F_max256KB, LZ4F_blockLinked, LZ4F_noContentChecksum, LZ4F_frame, 0, { 0, 0 } },
	0,
	0,
	{ 0, 0, 0, 0 },
};

size_t compressLZ4(Mat mat, char *destarray) {
    int buffer_size = mat.rows * mat.columns * 3;
    
    char *data = mat.data;
    size_t result_size = LZ4F_compressFrame(destarray, buffer_size, data, buffer_size, lz4_preferences);
    return result_size;
}

Mat decompressLZ4(char *buf, size_t size) {
    LZ4F_decompressionContext_t ctx;
    LZ4F_createDecompressionContext(ctx, LZ4_versionNumber());
    
    LZ4F_decompressOptions_t opt = {0, {0, 0, 0}};
    
    char *dest;
    size_t dest_size;
    LZ4F_decompress(&ctx, dest, &dest_size, buf, &size, &opt);
    
    Mat m = Mat(480, 640, CV_8UC3);
    m.data = dest;
    return m;
}