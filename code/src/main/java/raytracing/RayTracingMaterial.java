package raytracing;

import image.RGBA;

public class RayTracingMaterial {

  private RGBA color = RGBA.white;
  private RGBA ambientColor = RGBA.white;
  private RGBA reflectance = RGBA.white.times(0.2);
  private RGBA transparency = RGBA.white.times(0.4);
  private boolean transparent = false;

  private RGBA emission = RGBA.black;

  private double density = 1.0;
  private double roughness = 1;

  //region Getters and Setters...
  public RayTracingMaterial setColor(final RGBA color) {
    this.color = color;

    // TODO: Ideally, the following line should be added everywhere, where material is initialized.
    //  The ambient color is not necessarily the same as the material color.
    setAmbientColor(color);

    return this;
  }

  public RayTracingMaterial setAmbientColor(final RGBA color) {
    this.ambientColor = color;
    return this;
  }

  public RayTracingMaterial setReflectance(final RGBA reflectance) {
    this.reflectance = reflectance;
    return this;
  }

  public RayTracingMaterial setEmission(final RGBA emission) {
    this.emission = emission;
    return this;
  }

  public RayTracingMaterial setDensity(final double density) {
    this.density = density;
    return this;
  }

  public RayTracingMaterial setRoughness(final double roughness) {
    this.roughness = roughness;
    return this;
  }


  public RayTracingMaterial setTransparent(final boolean transparent) {
    this.transparent = transparent;
    return this;
  }


  public RayTracingMaterial setTransparency(final RGBA transparency) {
    this.transparency = transparency;
    return this;
  }

  public RGBA getColor() {
    return color;
  }

  public RGBA getAmbientColor() {
    return ambientColor;
  }

  public RGBA getReflectance() {
    return reflectance;
  }

  public RGBA getTransparency() {
    return transparency;
  }

  public boolean isTransparent() {
    return transparent;
  }

  public RGBA getEmission() {
    return emission;
  }

  public double getDensity() {
    return density;
  }

  public double getRoughness() {
    return roughness;
  }
  //endregion

  public static RayTracingMaterial Default() {
    return new RayTracingMaterial();
  }

  public static RayTracingMaterial DefaultColored(RGBA color) {
    return new RayTracingMaterial()
        .setColor(color)
        .setReflectance(RGBA.white.times(0.2));
  }

  public static RayTracingMaterial PerfectMirror() {
    return new RayTracingMaterial()
        .setColor(RGBA.black)
        .setReflectance(RGBA.white)
        .setRoughness(0.0);
  }

  public static RayTracingMaterial Transparent(double transparency, double reflectance,
      double density) {
    return new RayTracingMaterial()
        .setColor(RGBA.black)
        .setTransparent(true)
        .setTransparency(RGBA.white.times(transparency))
        .setReflectance(RGBA.white.times(reflectance))
        .setDensity(density);
  }

}
