package br.usp.ime.choreos.nodepoolmanager.utils
import br.usp.ime.choreos.nodepoolmanager.Configuration
import java.util.Properties
import org.jclouds.chef.ChefContext
import com.google.common.io.Files
import java.io.File
import com.google.common.base.Charsets
import org.jclouds.chef.ChefContextFactory
import com.google.common.collect.ImmutableSet
import com.google.inject.Module

class ChefHelper() {
  private val client = Configuration.get("CHEF_USER")
  private val pemFile: String = Configuration.get("CHEF_USER_KEY_FILE")

  private val credential = Files.toString(new File(pemFile), Charsets.UTF_8);
  private val overrides = new Properties();
  overrides.setProperty("chef.endpoint", Configuration.get("CHEF_ENDPOINT"));
  val chefContext: ChefContext = new ChefContextFactory().createContext(client, credential, ImmutableSet.of[Module](), overrides);

  def getChefContext = chefContext

}