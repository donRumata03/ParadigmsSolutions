from git import repo, UpdateProgress

from scripts.location_detector import tests_dir

tests_repo = repo.Repo(tests_dir)


def update_tests_repo():
	origin = [
		remote for remote in tests_repo.remotes if str(remote.name) == "origin"
	][0]

	print(f"Fetched tests from {origin.url} {origin.pull('master')[0].remote_ref_path}")


if __name__ == '__main__':
	update_tests_repo()
