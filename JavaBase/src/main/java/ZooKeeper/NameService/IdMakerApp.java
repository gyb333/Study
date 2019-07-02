package ZooKeeper.NameService;

import ZooKeeper.NameService.IdMaker.RemoveMethod;

public class IdMakerApp {

	public static void main(String[] args) throws Exception {
		
		IdMaker idMaker = new IdMaker("/NameService/IdGen", "ID");
		idMaker.start();

		try {
			for (int i = 0; i < 10; i++) {
				String id = idMaker.generateId(RemoveMethod.DELAY);
				System.out.println(id);

			}
		} finally {
			idMaker.stop();

		}
	}

}
