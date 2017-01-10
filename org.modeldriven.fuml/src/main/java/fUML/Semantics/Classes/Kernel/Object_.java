
/*
 * Initial version copyright 2008 Lockheed Martin Corporation, except  
 * as stated in the file entitled Licensing-Information. 
 * 
 * All modifications copyright 2009-2017 Data Access Technologies, Inc.
 *
 * Licensed under the Academic Free License version 3.0 
 * (http://www.opensource.org/licenses/afl-3.0.php), except as stated 
 * in the file entitled Licensing-Information. 
 */

package fUML.Semantics.Classes.Kernel;

import fUML.Debug;
import fUML.Syntax.Classes.Kernel.*;

import fUML.Semantics.CommonBehaviors.Communications.*;

public class Object_ extends fUML.Semantics.Classes.Kernel.ExtensionalValue {

	public fUML.Syntax.Classes.Kernel.Class_List types = new fUML.Syntax.Classes.Kernel.Class_List();
	public fUML.Semantics.CommonBehaviors.Communications.ObjectActivation objectActivation = null;

	public void startBehavior(
			fUML.Syntax.Classes.Kernel.Class_ classifier,
			fUML.Semantics.CommonBehaviors.BasicBehaviors.ParameterValueList inputs) {
		// Create an object activation for this object (if one does not already
		// exist) and start its behavior(s).

		// Debug.println("[startBehavior] On object...");

		if (this.objectActivation == null) {
			this.objectActivation = new ObjectActivation();
			this.objectActivation.object = this;
		}

		// Debug.println("[startBehavior] objectActivation = " +
		// objectActivation);

		this.objectActivation.startBehavior(classifier, inputs);
	} // startBehavior

	public fUML.Semantics.CommonBehaviors.BasicBehaviors.Execution dispatch(
			fUML.Syntax.Classes.Kernel.Operation operation) {
		// Dispatch the given operation to a method execution, using a dispatch
		// strategy.

		return ((DispatchStrategy) this.locus.factory.getStrategy("dispatch"))
				.dispatch(this, operation);
	} // dispatch

	public void send(
			fUML.Semantics.CommonBehaviors.Communications.EventOccurrence eventOccurrence) {
		// If the object is active, add the given event occurrence to the event
		// pool and signal that a new event occurrence has arrived.

		if (this.objectActivation != null) {
			this.objectActivation.send(eventOccurrence);
		}

	} // send

	public void destroy() {
		// Stop the object activation (if any), clear all types and destroy the
		// object as an extensional value.

		Debug.println("[destroy] object = " + this.identifier);

		if (this.objectActivation != null) {
			this.objectActivation.stop();
			this.objectActivation = null;
		}

		this.types.clear();
		super.destroy();
	} // destroy

	public void register(
			fUML.Semantics.CommonBehaviors.Communications.EventAccepter accepter) {
		// Register the given accept event accepter to wait for a dispatched
		// signal event.

		if (this.objectActivation != null) {
			this.objectActivation.register(accepter);
		}
	} // register

	public void unregister(
			fUML.Semantics.CommonBehaviors.Communications.EventAccepter accepter) {
		// Remove the given event accepter for the list of waiting event
		// accepters.

		if (this.objectActivation != null) {
			this.objectActivation.unregister(accepter);
		}
	} // unregister

	public fUML.Semantics.Classes.Kernel.Value copy() {
		// Create a new object that is a copy of this object at the same locus
		// as this object.
		// However, the new object will NOT have any object activation (i.e, its
		// classifier behaviors will not be started).

		Object_ newObject = (Object_) (super.copy());

		Class_List types = this.types;
		for (int i = 0; i < types.size(); i++) {
			Class_ type = types.getValue(i);
			newObject.types.addValue(type);
		}

		return newObject;

	} // copy
	
	public boolean equals(Value otherValue) {
		// Test if this object is equal to the otherValue.
		// To be equal, the otherValue must be the same object as this object.
		
		return this == otherValue;
	} // equals

	protected fUML.Semantics.Classes.Kernel.Value new_() {
		// Create a new object with no type, feature values or locus.

		return new Object_();
	} // new_

	public fUML.Syntax.Classes.Kernel.ClassifierList getTypes() {
		// Return the types of this object.

		ClassifierList types = new ClassifierList();
		Class_List myTypes = this.types;
		for (int i = 0; i < myTypes.size(); i++) {
			Class_ type = myTypes.getValue(i);
			types.addValue(type);
		}

		return types;
	} // getTypes

} // Object
