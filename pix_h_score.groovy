import qupath.lib.regions.RegionRequest
import qupath.lib.scripting.QP
import java.awt.image.BufferedImage

def server = QP.getCurrentServer()
def roi = QP.getSelectedROI()
double downsample = 1.0
def request = RegionRequest.createInstance(server.getPath(), downsample, roi)
BufferedImage img = server.readRegion(request)
int w = img.getWidth()
int h = img.getHeight()
println(w*h)
var raster = img.getRaster()
float[] px = new float[raster.getNumBands()]
threshold_heam=0.3
threshold_dab1=0.2
threshold_dab2=0.4
def n_heam = 0.0
def n_dab_h = 0.0
def n_dab_m = 0.0
def n_dab_l = 0.0
for (int y = 0; y < h; y++) {
    for (int x = 0; x < w; x++) {
        px = raster.getPixel(x, y, px)
        if (!(Float.isNaN(px[0]))){
            if (px[0] > threshold_heam){
                n_heam += 1
            }
        }
        if (!(Float.isNaN(px[1]))) {
            if (px[1] > threshold_dab2) {
                n_dab_h += 1
            } else if (px[1] > threshold_dab1) {
                n_dab_m += 1
            } else {
                n_dab_l += 1
            }
        }
    }
}
println(n_heam)
println(n_dab_h)
println(n_dab_m)
println(n_dab_l)
def pixH = 100*(3*n_dab_h+2*n_dab_m+1*n_dab_l) / (n_dab_h+n_dab_m+n_dab_l+n_heam)
println(pixH)
