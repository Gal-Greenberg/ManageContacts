
public interface IView <T extends PaneInterface> {
	
	public final static String[] FILE_TYPES = {"txt", "obj.dat", "byte.dat"};
	public final static String[] SORT = {"sort-field", "sort-count", "reverse"};
	public final static String[] FIELD = {"FIRST_NAME_FIELD", "LAST_NAME_FIELD", "PHONE_NUMBER_FIELD"};
	public final static String[] ORDER = {"asc", "desc"};
	
	void initView();
	
	void registerListener(IActionListener listener);
	
	T getMainPanel();
	
}
