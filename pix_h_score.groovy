import qupath.lib.images.servers.ColorTransforms
import qupath.lib.images.servers.ImageServer
import qupath.lib.regions.RegionRequest
import qupath.lib.scripting.QP
import java.awt.image.BufferedImage
// WARNING: DO NOT USE THIS
def server = QP.getCurrentServer()
def imageData = QP.getCurrentImageData()
def roi = QP.getSelectedROI()
double downsample = 1.0
def heamatox = ColorTransforms.createColorDeconvolvedChannel(imageData.getColorDeconvolutionStains(), 1)
def dab = ColorTransforms.createColorDeconvolvedChannel(imageData.getColorDeconvolutionStains(), 2)
def request = RegionRequest.createInstance(server.getPath(), downsample, roi)
BufferedImage img = server.readRegion(request)
def hem_values = heamatox.extractChannel(server as ImageServer<BufferedImage>, img)
def dab_values = dab.extractChannel(server as ImageServer<BufferedImage>, img)
println("heamatox = " + hem_values)
println("DAB = " + dab_values)
threshold_heam=0.3
threshold_dab=[0.1, 0.2, 0.3]
def n_heam = 0.0
def n_dab_h = 0.0
def n_dab_m = 0.0
def n_dab_l = 0.0
dab_values.eachWithIndex{ float entry, int i ->
    if (!(Float.isNaN(entry))&&!Float.isNaN(hem_values[i])) {
        if (entry > threshold_dab[2]) {
            n_dab_h += 1
        } else if (entry > threshold_dab[1]) {
            n_dab_m += 1
        } else if (entry > threshold_dab[0]) {
            n_dab_l += 1
        } else if(hem_values[i] > threshold_heam){
            n_heam += 1
        }
    }
}
println(n_dab_h)
println(n_dab_m)
println(n_dab_l)
println(n_heam)
def pixH = 100*(3*n_dab_h+2*n_dab_m+1*n_dab_l) / (n_dab_h+n_dab_m+n_dab_l+n_heam)
println(pixH)
