import java.io.File;

import org.napile.jvm.util.FileUtils;

/**
 * @author VISTALL
 * @date 2:58/02.02.2012
 */
public class Test
{
	public static void main(String... arg)
	{
		File f = new File("c:/windows/explorer.exe");

		System.out.println(FileUtils.getFileExtension(f));
	}
}
