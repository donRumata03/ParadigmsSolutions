from pathlib import Path
from typing import Iterable

import gitignore_parser
from location_detector import solutions_dir

test_detector = gitignore_parser.parse_gitignore(solutions_dir / ".testignore")


def filter_tests(paths: Iterable[Path]) -> Iterable[Path]:
	return filter(test_detector, paths)


def filter_out_tests(paths: Iterable[Path]) -> Iterable[Path]:
	return filter(lambda f: not test_detector(f), paths)

