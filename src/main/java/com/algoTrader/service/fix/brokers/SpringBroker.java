package com.algoTrader.service.fix.brokers;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.InitializingBean;

import com.algoTrader.service.fix.brokers.config.LogonAction;
import com.algoTrader.service.fix.brokers.config.LogoutAction;
import com.algoTrader.service.fix.filters.MessageModifierManager;
import com.algoTrader.service.fix.filters.MessageRouteManager;
import com.algoTrader.service.fix.utils.SpringSessionDescriptor;

/**
 * The Spring-based configuration of a single broker.
 * 
 */

public class SpringBroker implements InitializingBean {

	// INSTANCE DATA.

	private SpringSessionDescriptor mSessionDescriptor;

	private String mName;

	private String mId;

	private boolean mFixLogoutRequired;

	private MessageModifierManager mModifiers;

	private MessageRouteManager mRoutes;

	private MessageModifierManager mPreSendModifiers;

	private boolean islogin;

	private MessageModifierManager mResponseModifiers;

	private Collection<LogonAction> logonActions = new ArrayList<LogonAction>();

	private Collection<LogoutAction> logoutActions = new ArrayList<LogoutAction>();

	// INSTANCE METHODS.

	/**
	 * Sets the configuration of the receiver's QuickFIX/J session descriptor to
	 * the given one.
	 * 
	 * @param sessionDescriptor
	 *            The configuration.
	 */

	public void setDescriptor(SpringSessionDescriptor sessionDescriptor) {
		mSessionDescriptor = sessionDescriptor;
	}

	/**
	 * Returns the configuration of the receiver's QuickFIX/J session
	 * descriptor.
	 * 
	 * @return The configuration.
	 */

	public SpringSessionDescriptor getDescriptor() {
		return mSessionDescriptor;
	}

	/**
	 * Sets the receiver's name to the given value.
	 * 
	 * @param name
	 *            The name.
	 */

	public void setName(String name) {
		mName = name;
	}

	/**
	 * Returns the receiver's name.
	 * 
	 * @return The name.
	 */

	public String getName() {
		return mName;
	}

	/**
	 * Sets the receiver's broker ID to the given string form value.
	 * 
	 * @param id
	 *            The ID.
	 */

	public void setId(String id) {
		mId = id;
	}

	/**
	 * Returns the receiver's broker ID, in string form.
	 * 
	 * @return The ID.
	 */

	public String getId() {
		return mId;
	}

	/**
	 * Indicates if the broker requires FIX logout on disconnect.
	 * 
	 * @return a <code>boolean</code> value
	 */
	public boolean getFixLogoutRequired() {
		return mFixLogoutRequired;
	}

	/**
	 * Sets if the broker requires FIX logout on disconnect.
	 * 
	 * @param a
	 *            <code>boolean</code> value
	 */
	public void setFixLogoutRequired(boolean inLogout) {
		mFixLogoutRequired = inLogout;
	}

	/**
	 * Sets the receiver's message modifier manager to the given one.
	 * 
	 * @param modifiers
	 *            The manager. It may be null.
	 */

	public void setModifiers(MessageModifierManager modifiers) {
		mModifiers = modifiers;
	}

	/**
	 * Returns the receiver's message modifier manager.
	 * 
	 * @return The manager. It may be null.
	 */

	public MessageModifierManager getModifiers() {
		return mModifiers;
	}

	/**
	 * Sets the receiver's route manager to the given one.
	 * 
	 * @param routes
	 *            The manager. It may be null.
	 */

	public void setRoutes(MessageRouteManager routes) {
		mRoutes = routes;
	}

	/**
	 * Returns the receiver's route manager.
	 * 
	 * @return The manager. It may be null.
	 */

	public MessageRouteManager getRoutes() {
		return mRoutes;
	}

	/**
	 * Sets the receiver's pre-sending message modifier manager to the given
	 * one.
	 * 
	 * @param preSendModifiers
	 *            The manager. It may be null.
	 */

	public void setPreSendModifiers(MessageModifierManager preSendModifiers) {
		mPreSendModifiers = preSendModifiers;
	}

	/**
	 * Returns the receiver's pre-sending message modifier manager.
	 * 
	 * @return The manager. It may be null.
	 */

	public MessageModifierManager getPreSendModifiers() {
		return mPreSendModifiers;
	}

	public void setLogin(boolean login) {
		this.islogin = login;
	}

	public boolean getLogin() {
		return islogin;
	}

	/**
	 * Sets the receiver's response message modifier manager to the given one.
	 * 
	 * @param responseModifiers
	 *            The manager. It may be null.
	 */

	public void setResponseModifiers(MessageModifierManager responseModifiers) {
		mResponseModifiers = responseModifiers;
	}

	/**
	 * Returns the receiver's response message modifier manager.
	 * 
	 * @return The manager. It may be null.
	 */

	public MessageModifierManager getResponseModifiers() {
		return mResponseModifiers;
	}

	/**
	 * Get the logonActions value.
	 * 
	 * @return a <code>List&lt;LogonAction&gt;</code> value
	 */
	public Collection<LogonAction> getLogonActions() {
		return logonActions;
	}

	/**
	 * Get the logoutActions value.
	 * 
	 * @return a <code>Collection&lt;LogoutAction&gt;</code> value
	 */
	public Collection<LogoutAction> getLogoutActions() {
		return logoutActions;
	}

	/**
	 * Sets the logoutActions value.
	 * 
	 * @param a
	 *            <code>Collection&lt;LogoutAction&gt;</code> value
	 */
	public void setLogoutActions(Collection<LogoutAction> inLogoutActions) {
		logoutActions = inLogoutActions;
	}

	/**
	 * Sets the logonActions value.
	 * 
	 * @param a
	 *            <code>Collection&lt;LogonAction&gt;</code> value
	 */
	public void setLogonActions(Collection<LogonAction> inLogonActions) {
		logonActions = inLogonActions;
	}

	// InitializingBean.

	@Override
	public void afterPropertiesSet() throws Exception {
		if (getDescriptor() == null) {
			throw new Exception("No descriptor was set");
		}
		if (getName() == null) {
			throw new Exception("No name was set");
		}
		if (getId() == null) {
			throw new Exception("No ID was set");
		}
	}
}
