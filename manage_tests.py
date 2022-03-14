import os
from typing import Iterable

import gitignore_parser
from pathlib import Path
from sys import argv
import colorama
colorama.init(autoreset=True)



def list_files(root_directory: Path) -> Iterable[Path]:
	for (dirpath, dirnames, filenames) in os.walk(root_directory):
		for fname in filenames:
			yield Path(os.path.join(dirpath, fname))



def filter_tests(paths: Iterable[Path]) -> Iterable[Path]:
	return filter(test_detector, paths)


def filter_out_tests(paths: Iterable[Path]) -> Iterable[Path]:
	return filter(lambda f: not test_detector(f), paths)


assert len(argv) == 2
match argv[1]:
	case "list-tests":
		print(colorama.Fore.GREEN + "These files are tests:")
		for test_file in filter_tests(list_files(solutions_dir)):
			print(test_file.relative_to(solutions_dir))
	case "list-non-tests":
		print(colorama.Fore.GREEN + "These files are NOT tests:")
		for test_file in filter_out_tests(list_files(solutions_dir)):
			print(test_file.relative_to(solutions_dir))
	case "delete-tests":
		print(colorama.Fore.GREEN + "Deleting tests…")
		for test_file in filter_tests(list_files(solutions_dir)):
			print(f"Removing {test_file.relative_to(solutions_dir)}")
			os.remove(str(test_file))
		print(colorama.Fore.GREEN + "Done deleting tests!")
	case _:
		print(colorama.Fore.RED + "Unknown command!")

