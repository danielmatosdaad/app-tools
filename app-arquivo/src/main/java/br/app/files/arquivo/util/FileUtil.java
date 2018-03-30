package br.app.files.arquivo.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.PatternSyntaxException;

public class FileUtil {

	public static Path criarDiretorio(Class<?> clazz, Path caminhoAbsoluto) {

		if (Files.isDirectory(caminhoAbsoluto, LinkOption.NOFOLLOW_LINKS)) {
			Path caminho = Paths.get(caminhoAbsoluto.toUri());
			try {
				Path novoDiretorio = Paths.get(caminho.toString(), clazz.getSimpleName());
				if (Files.notExists(novoDiretorio)) {

					System.out.println(novoDiretorio.toString());
					Files.createDirectories(novoDiretorio);

				}

				return novoDiretorio;

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return null;
	}

	public static Path criarArquivo(Path caminho, Long identificador, String extensaoArquivo) {

		Path caminhoNovoArquivo = Paths.get(caminho.toString(), String.valueOf(identificador).concat(extensaoArquivo));

		try {

			if (Files.notExists(caminhoNovoArquivo)) {

				Files.createFile(caminhoNovoArquivo);
			}
			return caminhoNovoArquivo;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public static Path criarArquivo(Path caminho, String nomeArquivo, String extensaoArquivo) {

		Path caminhoNovoArquivo = Paths.get(caminho.toString(), String.valueOf(nomeArquivo).concat(extensaoArquivo));

		try {

			if (Files.notExists(caminhoNovoArquivo)) {

				Files.createFile(caminhoNovoArquivo);
			}
			return caminhoNovoArquivo;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public static boolean deletarDiretorio(Class<?> clazz, Path caminhoAbsoluto) {

		try {
			Path caminho = Paths.get(caminhoAbsoluto.toUri());
			Path caminhoDiretorio = Paths.get(caminho.toString(), clazz.getSimpleName());
			System.out.println(caminhoDiretorio.toString());
			return Files.deleteIfExists(caminhoDiretorio);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	public static boolean deletarArquivo(Class<?> clazz, Long identificador, Path caminhoAbsoluto,
			String extensaoArquivo) {

		try {
			Path caminho = Paths.get(caminhoAbsoluto.toUri());
			Path caminhoDiretorio = Paths.get(caminho.toString(), clazz.getSimpleName());
			Path caminhoArquivo = Paths.get(caminhoDiretorio.toString(),
					String.valueOf(identificador).concat(extensaoArquivo));
			System.out.println(caminhoArquivo.toString());
			return Files.deleteIfExists(caminhoArquivo);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;

	}

	public static boolean deletarDiretorioArquivoExtensao(Path caminho) {

		try {
			System.out.println("Deletando arquivo");
			System.out.println(caminho.toString());

			return Files.deleteIfExists(caminho);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;

	}

	public static void deletar(Path caminhoDiretorio) {

		try (DirectoryStream<Path> stream = Files.newDirectoryStream(caminhoDiretorio)) {

			for (Path path : stream) {

				if (path.toFile().isFile()) {
					System.out.println(path.getFileName());
					deletarDiretorioArquivoExtensao(path);
				}

			}
		} catch (PatternSyntaxException | DirectoryIteratorException | IOException e) {

			System.err.println(e);
		}
	}

	public static void deletar(Path caminhoDiretorio, String extensao) {

		try (DirectoryStream<Path> stream = Files.newDirectoryStream(caminhoDiretorio, "*".concat(extensao))) {

			for (Path path : stream) {

				if (path.toFile().isFile()) {
					System.out.println(path.getFileName());
					deletarDiretorioArquivoExtensao(path);
				}

			}
		} catch (PatternSyntaxException | DirectoryIteratorException | IOException e) {

			System.err.println(e);
		}
	}

	public static void deletarTodosDiretorios(Path caminhoDiretorio) {

		try (DirectoryStream<Path> stream = Files.newDirectoryStream(caminhoDiretorio)) {

			for (Path path : stream) {

				if (path.toFile().isDirectory()) {
					System.out.println(path.getFileName());
					deletar(path);
					deletarTodosDiretorios(path);
					deletarDiretorioArquivoExtensao(path);
				}

			}
		} catch (PatternSyntaxException | DirectoryIteratorException | IOException e) {

			System.err.println(e);
		}

	}

	public static void listarTodosDiretorios(Path caminhoDiretorio, List<Path> diretorios) {

		try (DirectoryStream<Path> stream = Files.newDirectoryStream(caminhoDiretorio)) {

			for (Path path : stream) {

				File file = path.toFile();
				if (file.isDirectory()) {
					diretorios.add(path);
					System.out.println(path.getFileName());
					listarTodosDiretorios(path, diretorios);
					System.out.println("Fim");
				}

			}
		} catch (PatternSyntaxException | DirectoryIteratorException | IOException e) {

			System.err.println(e);
		}
	}

	public static void buscarTodosArquivosDiretorio(Path caminhoDiretorio, List<File> arquivosDiretorio,
			String extensaoArquivo) {

		try (DirectoryStream<Path> stream = Files.newDirectoryStream(caminhoDiretorio)) {

			for (Path path : stream) {

				File file = path.toFile();
				if (file.isFile()) {

					if (file.getName().contains(extensaoArquivo)) {
						System.out.println("caminho: " + path.getFileName());
						arquivosDiretorio.add(file);
					}
				}
				if (file.isDirectory()) {

					buscarTodosArquivosDiretorio(path, arquivosDiretorio, extensaoArquivo);
				}

			}
		} catch (PatternSyntaxException | DirectoryIteratorException | IOException e) {

			System.err.println(e);
		}
	}

	public static Path buscarDiretorio(Path caminhoRaiz, String nomeDiretorio) {

		try (DirectoryStream<Path> stream = Files.newDirectoryStream(caminhoRaiz)) {

			for (Path path : stream) {

				File file = path.toFile();
				System.out.println(file.getName().toString());
				if (file.isDirectory()) {
					if (file.getName().contains(nomeDiretorio)) {
						System.out.println("achei");
						return path;
					}
					Path pathProcurado = buscarDiretorio(path, nomeDiretorio);
					if (pathProcurado != null) {
						return pathProcurado;
					}
				}

			}
		} catch (PatternSyntaxException | DirectoryIteratorException | IOException e) {

			System.err.println(e);
		}
		return null;
	}

	public static StringBuilder read(File file) {

		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader reader = Files.newBufferedReader(file.toPath(), Charset.defaultCharset());
			String line = null;
			while ((line = reader.readLine()) != null)
				sb.append(line);

		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return sb;
	}
}
