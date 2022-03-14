"""
There are three major repositories for GK's HTs:
	— Working repo
	— Tests: we can `pull origin master` and copy tests into working repo
		with rewriting and deleting all tests' sources from it && compiled sources
	— Presenting repo: we can copy non-tests into it
		(and commit by hand afterwards)

This is achieved by marking some source files in Working repo as tests
and others — as MY solutions. Use ./.testignore for «ignoring» tests
with gitignore syntax inside
"""

import os
import shutil
from typing import Iterable

import gitignore_parser
from pathlib import Path
from sys import argv
import colorama

from scripts.git_manager import update_tests_repo
from scripts.test_path_filterer import filter_tests, filter_out_tests, \
	test_detector

colorama.init(autoreset=True)

from scripts.location_detector import *


def delete_tests():
	print(colorama.Fore.GREEN + "Deleting tests…")
	for test_file in filter_tests(list_files(solutions_dir)):
		print(f"Removing {test_file.relative_to(solutions_dir)}")
		os.remove(str(test_file))
	print(colorama.Fore.GREEN + "Done deleting tests!")


def update_tests():
	print(colorama.Fore.GREEN + "Updating tests…")

	delete_tests()
	update_tests_repo()

	shutil.copytree(
		tests_dir / "artifacts", solutions_dir / "compiledTests",
		dirs_exist_ok=True
	)
	shutil.copytree(
		tests_dir / "java", solutions_dir / "src",
		dirs_exist_ok=True
	)

	print(colorama.Fore.GREEN + "Done updating tests!")


def present_solutions(subfolder: str):
	solution_path = solutions_dir / "src" / subfolder
	assert solution_path.exists()

	# Ensure target exists and is clear
	presentation_path = presentation_dir / "java-solutions" / subfolder
	if presentation_path.exists():
		shutil.rmtree(str(presentation_path))
	# if not presentation_path.exists():
	# 	os.mkdir(str(presentation_path))

	print(f"Copying {subfolder} solution files:")

	def inspect(full_path):
		is_test = test_detector(full_path)
		if not Path(full_path).is_dir():
			print(f"{Path(full_path).relative_to(solution_path)}: {'TEST' if is_test else 'SOLUTION'}")
		return is_test

	# Copy excluding tests
	shutil.copytree(
		str(solution_path),
		str(presentation_path),
		ignore=lambda path, filenames: list(filter(lambda name: inspect(os.path.join(path, name)), filenames))
	)

assert len(argv) >= 2
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
		delete_tests()
	case "update-tests":
		# TODO:
		#  1. if COPIED test appears first time and doesn't match .testignore, add it there
		#  2. Enable partial test updating (by folder)
		update_tests()
	case "present-solutions":
		assert len(argv) == 3
		present_solutions(argv[2])
	case _:
		print(colorama.Fore.RED + "Unknown command!")
