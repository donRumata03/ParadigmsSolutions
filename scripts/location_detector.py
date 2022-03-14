# Set up dirs
import os
from pathlib import Path
from typing import Iterable

import colorama

colorama.init(autoreset=True)


def list_files(root_directory: Path) -> Iterable[Path]:
	for (dirpath, dirnames, filenames) in os.walk(root_directory):
		for fname in filenames:
			yield Path(os.path.join(dirpath, fname))


def check_dir(required_path: Path):
	if not required_path.exists() or not required_path.is_dir():
		print(colorama.Fore.RED + f"Directory \"{required_path}\" must exist")
		exit(1)


paradigms_dir = Path(__file__).resolve().parent.parent.parent

solutions_dir = paradigms_dir / "ParadigmsSolutions"
tests_dir = paradigms_dir / "paradigms-2022"
presentation_dir = paradigms_dir / "paradigms"


for d in (solutions_dir, tests_dir, presentation_dir):
	check_dir(d)

