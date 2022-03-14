# Set up dirs
from pathlib import Path
import colorama
colorama.init(autoreset=True)

import gitignore_parser


def check_dir(required_path: Path):
	if not required_path.exists() or not required_path.is_dir():
		print(colorama.Fore.RED + f"Directory \"{required_path}\" must exist")
		exit(1)



paradigms_dir = Path(__file__).resolve().parent.parent.parent

solutions_dir = paradigms_dir / "ParadigmsSolutions"
tests_dir = paradigms_dir / "paradigms-2022"
repo_dir = paradigms_dir / "paradigms"



for d in (solutions_dir, tests_dir, repo_dir):
	check_dir(d)

test_detector = gitignore_parser.parse_gitignore(solutions_dir / ".testignore")
