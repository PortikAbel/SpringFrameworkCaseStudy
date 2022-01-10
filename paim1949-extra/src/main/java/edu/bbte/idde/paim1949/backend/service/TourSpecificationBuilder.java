package edu.bbte.idde.paim1949.backend.service;

import edu.bbte.idde.paim1949.backend.dto.incoming.requestparam.TourFilterDto;
import edu.bbte.idde.paim1949.backend.model.Tour;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class TourSpecificationBuilder {

    private static Specification<Tour> signShapeIs(String signShape) {
        return (tour, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(tour.get("signShape"), Tour.SignColour.valueOf(signShape));
    }

    private static Specification<Tour> signColourIs(String signColour) {
        return (tour, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(tour.get("signColour"), Tour.SignColour.valueOf(signColour));
    }

    private static Specification<Tour> minDistance(Float distanceInKm) {
        return (tour, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.ge(tour.get("distanceInKm"), distanceInKm);
    }

    private static Specification<Tour> maxDistance(Float distanceInKm) {
        return (tour, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.le(tour.get("distanceInKm"), distanceInKm);
    }

    private static Specification<Tour> minElevation(Integer elevationInM) {
        return (tour, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.ge(tour.get("elevationInM"), elevationInM);
    }

    private static Specification<Tour> maxElevation(Integer elevationInM) {
        return (tour, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.le(tour.get("elevationInM"), elevationInM);
    }

    private static Specification<Tour> minDays(Integer daysRecommended) {
        return (tour, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.ge(tour.get("daysRecommended"), daysRecommended);
    }

    private static Specification<Tour> maxDays(Integer daysRecommended) {
        return (tour, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.le(tour.get("daysRecommended"), daysRecommended);
    }

    private Specification<Tour> parseDaysFilters(TourFilterDto tourFilterDto) {
        Specification<Tour> spec = Specification.where(null);
        if (tourFilterDto.getMinDaysRecommended() != null) {
            spec = spec.and(minDays(tourFilterDto.getMinDaysRecommended()));
        }
        if (tourFilterDto.getMaxDaysRecommended() != null) {
            spec = spec.and(maxDays(tourFilterDto.getMaxDaysRecommended()));
        }
        return spec;
    }

    private Specification<Tour> parseElevationFilters(TourFilterDto tourFilterDto) {
        Specification<Tour> spec = Specification.where(null);
        if (tourFilterDto.getMinElevation() != null) {
            spec = spec.and(minElevation(tourFilterDto.getMinElevation()));
        }
        if (tourFilterDto.getMaxElevation() != null) {
            spec = spec.and(maxElevation(tourFilterDto.getMaxElevation()));
        }
        return spec;
    }

    private Specification<Tour> parseDistanceFilters(TourFilterDto tourFilterDto) {
        Specification<Tour> spec = Specification.where(null);
        if (tourFilterDto.getMinDistance() != null) {
            spec = spec.and(minDistance(tourFilterDto.getMinDistance()));
        }
        if (tourFilterDto.getMaxDistance() != null) {
            spec = spec.and(maxDistance(tourFilterDto.getMaxDistance()));
        }
        return spec;
    }

    public Specification<Tour> tourFilterToSpecification(TourFilterDto tourFilterDto) {
        Specification<Tour> spec = Specification.where(null);
        if (tourFilterDto.getSignShapes() != null) {
            Specification<Tour> signShapeSpec = Specification.where(null);
            for (String signShape: tourFilterDto.getSignShapes()) {
                signShapeSpec = signShapeSpec.or(signShapeIs(signShape));
            }
            spec = spec.and(signShapeSpec);
        }
        if (tourFilterDto.getSignColors() != null) {
            Specification<Tour> signColorSpecification = Specification.where(null);
            for (String signColor: tourFilterDto.getSignColors()) {
                signColorSpecification = signColorSpecification.or(signColourIs(signColor));
            }
            spec = spec.and(signColorSpecification);
        }
        spec = spec.and(parseDaysFilters(tourFilterDto));
        spec = spec.and(parseElevationFilters(tourFilterDto));
        spec = spec.and(parseDistanceFilters(tourFilterDto));
        return spec;
    }
}
