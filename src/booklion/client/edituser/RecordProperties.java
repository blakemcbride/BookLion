package booklion.client.edituser;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

public interface RecordProperties extends PropertyAccess<Record> {
	
	ValueProvider<Record, Integer> recNo();

//	ModelKeyProvider<UserData> userId();
	ValueProvider<Record, Long> userId();
	
	ValueProvider<Record, String> admin();

	ValueProvider<Record, String> lname();

	ValueProvider<Record, String> fname();

	ValueProvider<Record, String> email();
	
	ValueProvider<Record, String> password();
	
	ValueProvider<Record, Integer> birthDate();
	
	ValueProvider<Record, Character> sex();
}