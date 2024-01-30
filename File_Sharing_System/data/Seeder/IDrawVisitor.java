/**
 * 
 */
package acsse.csc2a.model;

import acsse.csc2a.model.CandidatePlanet;
import acsse.csc2a.model.NonCandidatePlanet;

/**
 * @author themb
 *
 */
public interface IDrawVisitor {

	public void visit(CandidatePlanet CandidatePlanet);
	public void visit(NonCandidatePlanet NonCandidatePlanet);
}
