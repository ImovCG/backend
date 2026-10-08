package com.imovcg.back.util;

import java.util.Locale;

public enum UniversidadeCampinaGrande {
    UFCG(-7.2142430, -35.9077467),
    UEPB(-7.2099317, -35.9152810),
    IFPB(-7.2401400, -35.9154358);

    private static final double RAIO_TERRA_KM = 6371.0088;

    private final double latitude;
    private final double longitude;

    UniversidadeCampinaGrande(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public static UniversidadeCampinaGrande from(String nome) {
        return valueOf(nome.trim().toUpperCase(Locale.ROOT));
    }

    public double distanciaEmKm(double latitude, double longitude) {
        double diferencaLatitude = Math.toRadians(latitude - this.latitude);
        double diferencaLongitude = Math.toRadians(longitude - this.longitude);
        double latitudeImovelRad = Math.toRadians(latitude);
        double latitudeUniversidadeRad = Math.toRadians(this.latitude);

        double haversine = Math.pow(Math.sin(diferencaLatitude / 2), 2)
                + Math.cos(latitudeImovelRad) * Math.cos(latitudeUniversidadeRad)
                * Math.pow(Math.sin(diferencaLongitude / 2), 2);
        return 2 * RAIO_TERRA_KM * Math.asin(Math.sqrt(haversine));
    }
}
