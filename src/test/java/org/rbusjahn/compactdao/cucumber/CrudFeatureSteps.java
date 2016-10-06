package org.rbusjahn.compactdao.cucumber;

import org.junit.Assert;
import org.rbusjahn.compactdao.InMemoryConnectionSettings;
import org.rbusjahn.compactdao.cucumber.testmodel.PersonDao;
import org.rbusjahn.compactdao.cucumber.testmodel.PersonEntity;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class CrudFeatureSteps {

	private PersonEntity mikeMiller;

	private PersonDao cut;

	@Given("^A model entity$")
	public void a_model_entity() throws Throwable {
		mikeMiller = new PersonEntity();
		mikeMiller.setFirstname("Mike");
		mikeMiller.setLastname("Miller");

	}

	@Given("^A crud DAO$")
	public void a_crud_DAO() throws Throwable {
		cut = new PersonDao();
		cut.init(new InMemoryConnectionSettings());
		cut.createTable();
	}

	@When("^a model entity is persisted$")
	public void a_model_entity_is_persisted() throws Throwable {
		cut.save(mikeMiller);
	}

	@Then("^the model entity has a database ID$")
	public void the_model_entity_has_a_database_ID() throws Throwable {
		Assert.assertNotNull(mikeMiller.getId());
	}

	@Then("^the model entity can be reloaded from database$")
	public void the_model_entity_can_be_reloaded_from_database() throws Throwable {
		final PersonEntity mikeMillerReloaded = cut.getById(mikeMiller.getId());
		Assert.assertNotNull(mikeMillerReloaded);
		Assert.assertEquals(mikeMiller.getId(), mikeMillerReloaded.getId());
	}

	@When("^a model is deleted$")
	public void a_model_is_deleted() throws Throwable {
		cut.delete(mikeMiller);
	}

	@Then("^I cannot find a model entity by its ID anymore\\.$")
	public void i_cannot_find_a_model_entity_by_its_ID_anymore() throws Throwable {
		Assert.assertNull(cut.getById(mikeMiller.getId()));
	}

	@When("^a model entity is loaded$")
	public void a_model_entity_is_loaded() throws Throwable {
		cut.save(mikeMiller);
		mikeMiller = cut.getById(mikeMiller.getId());
	}

	@Then("^the model entity contains all database table fields$")
	public void the_model_entity_has_a_all_the_database_table_field_are_loaded() throws Throwable {
		Assert.assertEquals("Mike", mikeMiller.getFirstname());
		Assert.assertEquals("Miller", mikeMiller.getLastname());
	}

	@Then("^the model entity id is loaded$")
	public void the_model_entity_id_is_loaded() throws Throwable {
		Assert.assertNotNull(mikeMiller.getId());
	}

	@When("^that model entity is updated and reloaded$")
	public void that_model_entity_is_updated_and_reloaded() throws Throwable {
		this.a_model_entity();
		this.a_model_entity_is_persisted();
		mikeMiller = cut.getById(mikeMiller.getId());
		mikeMiller.setLastname("Mad-Miller");
		cut.save(mikeMiller);
	}

	@When("^a model entity is reloaded$")
	public void a_model_entity_is_reloaded() throws Throwable {
		mikeMiller = cut.getById(mikeMiller.getId());
	}

	@Then("^The previous made changes are applied to that model entity\\.$")
	public void the_previous_made_changes_are_applied_to_that_model_entity() throws Throwable {
		mikeMiller = cut.getById(mikeMiller.getId());
		Assert.assertEquals("Mad-Miller", mikeMiller.getLastname());
	}

}
