/*
 * Copyright (c) SiteWhere, LLC. All rights reserved. http://www.sitewhere.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.sitewhere.grpc.model.converter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.sitewhere.grpc.model.CommonModel;
import com.sitewhere.grpc.model.CommonModel.GDateRangeSearchCriteria;
import com.sitewhere.grpc.model.CommonModel.GDeviceAssignmentStatus;
import com.sitewhere.grpc.model.CommonModel.GEntityInformation;
import com.sitewhere.grpc.model.CommonModel.GLocation;
import com.sitewhere.grpc.model.CommonModel.GOptionalDouble;
import com.sitewhere.grpc.model.CommonModel.GPaging;
import com.sitewhere.grpc.model.CommonModel.GUUID;
import com.sitewhere.grpc.model.CommonModel.GUserReference;
import com.sitewhere.rest.model.common.Location;
import com.sitewhere.rest.model.common.SiteWhereEntity;
import com.sitewhere.rest.model.search.DateRangeSearchCriteria;
import com.sitewhere.rest.model.search.SearchCriteria;
import com.sitewhere.spi.SiteWhereException;
import com.sitewhere.spi.common.ILocation;
import com.sitewhere.spi.common.ISiteWhereEntity;
import com.sitewhere.spi.device.DeviceAssignmentStatus;
import com.sitewhere.spi.search.IDateRangeSearchCriteria;
import com.sitewhere.spi.search.ISearchCriteria;

/**
 * Convert between SiteWhere API model and GRPC model.
 * 
 * @author Derek
 */
public class CommonModelConverter {

    /**
     * Convert GRPC timestamp to date.
     * 
     * @param grpc
     * @return
     * @throws SiteWhereException
     */
    public static Date asApiDate(long grpc) throws SiteWhereException {
	if (grpc == 0) {
	    return null;
	}
	return new Date(grpc);
    }

    /**
     * Convert date to GRPC value.
     * 
     * @param date
     * @return
     * @throws SiteWhereException
     */
    public static long asGrpcDate(Date date) throws SiteWhereException {
	if (date == null) {
	    return 0;
	}
	return date.getTime();
    }

    /**
     * Convert location from GRPC to API.
     * 
     * @param grpc
     * @return
     * @throws SiteWhereException
     */
    public static Location asApiLocation(GLocation grpc) throws SiteWhereException {
	Location api = new Location();
	api.setLatitude(grpc.hasLatitude() ? grpc.getLatitude().getValue() : 0.0);
	api.setLongitude(grpc.hasLongitude() ? grpc.getLongitude().getValue() : 0.0);
	api.setElevation(grpc.hasElevation() ? grpc.getElevation().getValue() : 0.0);
	return api;
    }

    /**
     * Convert locations from GRPC to API.
     * 
     * @param grpcs
     * @return
     * @throws SiteWhereException
     */
    public static List<Location> asApiLocations(List<GLocation> grpcs) throws SiteWhereException {
	List<Location> api = new ArrayList<Location>();
	for (GLocation grpc : grpcs) {
	    api.add(CommonModelConverter.asApiLocation(grpc));
	}
	return api;
    }

    /**
     * Convert location from API to GRPC.
     * 
     * @param api
     * @return
     * @throws SiteWhereException
     */
    public static GLocation asGrpcLocation(ILocation api) throws SiteWhereException {
	GLocation.Builder grpc = GLocation.newBuilder();
	if (api.getLatitude() != null) {
	    grpc.setLatitude(GOptionalDouble.newBuilder().setValue(api.getLatitude()).build());
	}
	if (api.getLongitude() != null) {
	    grpc.setLongitude(GOptionalDouble.newBuilder().setValue(api.getLongitude()).build());
	}
	if (api.getElevation() != null) {
	    grpc.setElevation(GOptionalDouble.newBuilder().setValue(api.getElevation()).build());
	}
	return grpc.build();
    }

    /**
     * Convert locations from API to GRPC.
     * 
     * @param apis
     * @return
     * @throws SiteWhereException
     */
    public static List<GLocation> asGrpcLocations(List<? extends ILocation> apis) throws SiteWhereException {
	List<GLocation> grpcs = new ArrayList<GLocation>();
	for (ILocation api : apis) {
	    grpcs.add(CommonModelConverter.asGrpcLocation(api));
	}
	return grpcs;
    }

    /**
     * Convert GRPC paging block into generic search criteria.
     * 
     * @param paging
     * @return
     * @throws SiteWhereException
     */
    public static SearchCriteria asApiSearchCriteria(GPaging paging) throws SiteWhereException {
	return new SearchCriteria(paging.getPageNumber(), paging.getPageSize());
    }

    /**
     * Convert date range search criteria from GRPC to API.
     * 
     * @param grpc
     * @return
     * @throws SiteWhereException
     */
    public static DateRangeSearchCriteria asDateRangeSearchCriteria(GDateRangeSearchCriteria grpc)
	    throws SiteWhereException {
	Date startDate = CommonModelConverter.asApiDate(grpc.getStartDate());
	Date endDate = CommonModelConverter.asApiDate(grpc.getEndDate());
	return new DateRangeSearchCriteria(grpc.getPageNumber(), grpc.getPageSize(), startDate, endDate);
    }

    /**
     * Convert date range search criteria from API to GRPC.
     * 
     * @param api
     * @return
     * @throws SiteWhereException
     */
    public static GDateRangeSearchCriteria asGrpcDateRangeSearchCriteria(IDateRangeSearchCriteria api)
	    throws SiteWhereException {
	GDateRangeSearchCriteria.Builder grpc = GDateRangeSearchCriteria.newBuilder();
	grpc.setPageNumber(api.getPageNumber());
	grpc.setPageSize(api.getPageSize());
	grpc.setStartDate(CommonModelConverter.asGrpcDate(api.getStartDate()));
	grpc.setEndDate(CommonModelConverter.asGrpcDate(api.getEndDate()));
	return grpc.build();
    }

    /**
     * Convert paging information from API to GRPC.
     * 
     * @param api
     * @return
     * @throws SiteWhereException
     */
    public static GPaging asGrpcPaging(ISearchCriteria api) throws SiteWhereException {
	GPaging.Builder grpc = GPaging.newBuilder();
	grpc.setPageNumber(api.getPageNumber());
	grpc.setPageSize(api.getPageSize());
	return grpc.build();
    }

    /**
     * Craete {@link GEntityInformation} from API entity information.
     * 
     * @param api
     * @return
     * @throws SiteWhereException
     */
    public static GEntityInformation asGrpcEntityInformation(ISiteWhereEntity api) throws SiteWhereException {
	GEntityInformation.Builder grpc = CommonModel.GEntityInformation.newBuilder();
	if (api.getCreatedBy() != null) {
	    GUserReference.Builder ref = GUserReference.newBuilder();
	    ref.setUsername(api.getCreatedBy());
	    grpc.setCreatedBy(ref);
	}
	grpc.setCreatedDate(CommonModelConverter.asGrpcDate(api.getCreatedDate()));
	if (api.getUpdatedBy() != null) {
	    GUserReference.Builder ref = GUserReference.newBuilder();
	    ref.setUsername(api.getUpdatedBy());
	    grpc.setUpdatedBy(ref);
	}
	grpc.setUpdatedDate(CommonModelConverter.asGrpcDate(api.getUpdatedDate()));
	return grpc.build();
    }

    /**
     * Set API entity information from {@link GEntityInformation}.
     * 
     * @param api
     * @param grpc
     * @throws SiteWhereException
     */
    public static void setEntityInformation(SiteWhereEntity api, GEntityInformation grpc) throws SiteWhereException {
	if (grpc != null) {
	    api.setCreatedBy(grpc.hasCreatedBy() ? grpc.getCreatedBy().getUsername() : null);
	    api.setCreatedDate(CommonModelConverter.asApiDate(grpc.getCreatedDate()));
	    api.setUpdatedBy(grpc.hasUpdatedBy() ? grpc.getUpdatedBy().getUsername() : null);
	    api.setUpdatedDate(CommonModelConverter.asApiDate(grpc.getUpdatedDate()));
	}
    }

    /**
     * Convert UUID from GRPC to API.
     * 
     * @param grpc
     * @return
     * @throws SiteWhereException
     */
    public static UUID asApiUuid(GUUID grpc) throws SiteWhereException {
	if (grpc == null) {
	    return null;
	}
	return new UUID(grpc.getMsb(), grpc.getLsb());
    }

    /**
     * Convert UUIDs from GRPC to API.
     * 
     * @param grpcs
     * @return
     * @throws SiteWhereException
     */
    public static List<UUID> asApiUuids(List<GUUID> grpcs) throws SiteWhereException {
	List<UUID> api = new ArrayList<UUID>();
	for (GUUID grpc : grpcs) {
	    api.add(CommonModelConverter.asApiUuid(grpc));
	}
	return api;
    }

    /**
     * Convert UUID from API to GRPC.
     * 
     * @param api
     * @return
     * @throws SiteWhereException
     */
    public static GUUID asGrpcUuid(UUID api) throws SiteWhereException {
	if (api == null) {
	    return null;
	}
	GUUID.Builder grpc = GUUID.newBuilder();
	grpc.setMsb(api.getMostSignificantBits());
	grpc.setLsb(api.getLeastSignificantBits());
	return grpc.build();
    }

    /**
     * Convert UUIDs from API to GRPC.
     * 
     * @param apis
     * @return
     * @throws SiteWhereException
     */
    public static List<GUUID> asGrpcUuids(List<UUID> apis) throws SiteWhereException {
	List<GUUID> grpcs = new ArrayList<GUUID>();
	for (UUID api : apis) {
	    grpcs.add(CommonModelConverter.asGrpcUuid(api));
	}
	return grpcs;
    }

    /**
     * Convert device assignment status from GRPC to API.
     * 
     * @param grpc
     * @return
     * @throws SiteWhereException
     */
    public static DeviceAssignmentStatus asApiDeviceAssignmentStatus(GDeviceAssignmentStatus grpc)
	    throws SiteWhereException {
	switch (grpc) {
	case ASSN_STATUS_ACTIVE:
	    return DeviceAssignmentStatus.Active;
	case ASSN_STATUS_MISSING:
	    return DeviceAssignmentStatus.Missing;
	case ASSN_STATUS_RELEASED:
	    return DeviceAssignmentStatus.Released;
	case ASSN_STATUS_UNSPECIFIED:
	    return null;
	case UNRECOGNIZED:
	    throw new SiteWhereException("Unknown device assignment status: " + grpc.name());
	}
	return null;
    }

    /**
     * Convert device assignment status from API to GRPC.
     * 
     * @param api
     * @return
     * @throws SiteWhereException
     */
    public static GDeviceAssignmentStatus asGrpcDeviceAssignmentStatus(DeviceAssignmentStatus api)
	    throws SiteWhereException {
	if (api == null) {
	    return GDeviceAssignmentStatus.ASSN_STATUS_UNSPECIFIED;
	}
	switch (api) {
	case Active:
	    return GDeviceAssignmentStatus.ASSN_STATUS_ACTIVE;
	case Missing:
	    return GDeviceAssignmentStatus.ASSN_STATUS_MISSING;
	case Released:
	    return GDeviceAssignmentStatus.ASSN_STATUS_RELEASED;
	}
	throw new SiteWhereException("Unknown device assignment status: " + api.name());
    }
}