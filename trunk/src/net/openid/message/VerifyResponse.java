/*
 * Copyright 2006-2007 Sxip Identity Corporation
 */

package net.openid.message;

import org.apache.log4j.Logger;

import java.util.List;
import java.util.Arrays;

/**
 * @author Marius Scurtescu, Johnny Bufu
 */
public class VerifyResponse extends Message
{
    private static Logger _log = Logger.getLogger(VerifyResponse.class);
    private static final boolean DEBUG = _log.isDebugEnabled();

    protected final static List requiredFields = Arrays.asList(new String[] {
            "is_valid"
    });

    protected final static List optionalFields = Arrays.asList(new String[] {
            "ns",
            "invalidate_handle"
    });

    protected VerifyResponse(boolean compatibility)
    {
        setSignatureVerified(false);

        if (! compatibility)
            set("ns", OPENID2_NS);
    }

    protected VerifyResponse(ParameterList params)
    {
        super(params);
    }

    public static VerifyResponse createVerifyResponse(boolean compatibility)
            throws MessageException
    {
        VerifyResponse resp = new VerifyResponse(compatibility);

        if (! resp.isValid()) throw new MessageException(
                "Invalid set of parameters for the requested message type");

        if (DEBUG) _log.debug("Created verification response "
                              + resp.keyValueFormEncoding());

        return resp;
    }

    public static VerifyResponse createVerifyResponse(ParameterList params)
            throws MessageException
    {
        VerifyResponse resp = new VerifyResponse(params);

        if (! resp.isValid()) throw new MessageException(
                "Invalid set of parameters for the requested message type");

        if (DEBUG) _log.debug("Created verification response "
                              + resp.keyValueFormEncoding());

        return resp;
    }

    public List getRequiredFields()
    {
        return requiredFields;
    }

    public boolean isVersion2()
    {
        return hasParameter("ns") && OPENID2_NS.equals(getParameterValue("ns"));
    }

    public void setSignatureVerified(boolean verified)
    {
        set("is_valid", verified ? "true" : "false");
    }

    public boolean isSignatureVerified()
    {
        return "true".equals(getParameterValue("is_valid"));
    }

    public void setInvalidateHandle(String handle)
    {
        set("invalidate_handle", handle);
    }

    public String getInvalidateHandle()
    {
        return getParameterValue("invalidate_handle");
    }

    public boolean isValid()
    {
        if (! super.isValid()) return false;

        if (! "true".equals(getParameterValue("is_valid")) &&
                ! "false".equals(getParameterValue("is_valid")) )
        {
            _log.warn("Invalid is_valid value in verification response: "
                      + getParameterValue("is_valid"));
            return false;
        }

        return true;
    }
}