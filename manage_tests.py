import os
from typing import Iterable

import gitignore_parser
from pathlib import Path
from sys import argv
import colorama
colorama.init(autoreset=True)


def check_dir(required_path: Path):
	if not required_path.exists() or not required_path.is_dir():
		print(colorama.Fore.RED + f"Directory \"{required_path}\" must exist")
		exit(1)


def list_files(root_directory: Path) -> Iterable[Path]:
	for (dirpath, dirnames, filenames) in os.walk(root_directory):
		for fname in filenames:
			yield Path(os.path.join(dirpath, fname))


# Set up dirs
paradigms_dir = Path(__file__).resolve().parent.parent

solutions_dir = paradigms_dir / "ParadigmsSolutions"
tests_dir = paradigms_dir / "paradigms-2022"
repo_dir = paradigms_dir / "paradigms"

for d in (solutions_dir, tests_dir, repo_dir):
	check_dir(d)

test_detector = gitignore_parser.parse_gitignore(solutions_dir / ".testignore")


def filter_tests(paths: Iterable[Path]) -> Iterable[Path]:
	return filter(test_detector, paths)


def filter_out_tests(paths: Iterable[Path]) -> Iterable[Path]:
	return filter(lambda f: not test_detector(f), paths)


assert len(argv) == 2
match argv[1]:
	case "delete-tests":
		print(colorama.Fore.GREEN + "Deleting tests…")
		for test_file in filter_tests(list_files(paradigms_dir)):
			print(f"Removing {test_file.relative_to(solutions_dir)}")
			os.remove(str(test_file))
		print(colorama.Fore.GREEN + "Done deleting tests!")
	case _:
		print(colorama.Fore.RED + "Unknown command!")

